#!/usr/bin/env ruby
# frozen_string_literal: true

require 'json'
require 'net/http'
require 'optparse'
require 'uri'
require 'fileutils'

options = {
  api_url: ENV.fetch('AULMS_API_URL', 'http://localhost:8080'),
  token: ENV['AULMS_API_TOKEN'],
  changed_files: nil,
  response_json: nil,
  output_json: 'build/reports/aulms-pr-review.json',
  output_md: 'build/reports/aulms-pr-review.md',
  fail_on: 'error',
  pull_request_id: ENV['PR_NUMBER'] || ENV['GITHUB_REF_NAME'] || 'local',
  repository: ENV['GITHUB_REPOSITORY'] || 'local',
}

OptionParser.new do |parser|
  parser.banner = 'Usage: ruby scripts/aulms-pr-review.rb [options]'
  parser.on('--api-url URL', 'AULMS API base URL') { |value| options[:api_url] = value }
  parser.on('--token TOKEN', 'Bearer token') { |value| options[:token] = value }
  parser.on('--changed-files PATH', 'Changed files list') { |value| options[:changed_files] = value }
  parser.on('--response-json PATH', 'Use existing PR review response JSON instead of API call') { |value| options[:response_json] = value }
  parser.on('--output-json PATH', 'JSON report output') { |value| options[:output_json] = value }
  parser.on('--output-md PATH', 'Markdown report output') { |value| options[:output_md] = value }
  parser.on('--fail-on LEVEL', 'none, warning, error') { |value| options[:fail_on] = value }
  parser.on('--pull-request-id ID', 'Pull request id') { |value| options[:pull_request_id] = value }
  parser.on('--repository NAME', 'Repository name') { |value| options[:repository] = value }
end.parse!

SUPPORTED_EXTENSIONS = {
  '.yaml' => 'OPENAPI',
  '.yml' => 'OPENAPI',
  '.ddl' => 'DDL',
  '.sql' => 'SQL',
  '.kt' => 'KOTLIN',
  '.java' => 'JAVA',
  '.ts' => 'TYPESCRIPT',
  '.tsx' => 'TYPESCRIPT',
  '.feature' => 'TEST',
}.freeze

EXCLUDED_PREFIXES = [
  'generated/',
  'build/',
  '.gradle/',
  'node_modules/',
  '.next/',
].freeze

def ensure_parent(path)
  dir = File.dirname(path)
  return if dir == '.' || Dir.exist?(dir)

  FileUtils.mkdir_p(dir)
end

def supported_file?(path)
  return false if EXCLUDED_PREFIXES.any? { |prefix| path.start_with?(prefix) }

  SUPPORTED_EXTENSIONS.key?(File.extname(path))
end

def source_type(path)
  SUPPORTED_EXTENSIONS.fetch(File.extname(path), 'AUTO')
end

def changed_files(path)
  raise 'changed files path is required' if path.nil? || path.empty?
  raise "changed files path not found: #{path}" unless File.exist?(path)

  File.readlines(path, chomp: true)
      .map(&:strip)
      .reject(&:empty?)
      .select { |file_path| supported_file?(file_path) && File.file?(file_path) }
end

def build_request(options)
  files = changed_files(options[:changed_files]).map do |file_path|
    {
      filePath: file_path,
      content: File.read(file_path),
      sourceType: source_type(file_path),
    }
  end

  {
    pullRequestId: options[:pull_request_id],
    repository: options[:repository],
    failOnWarning: options[:fail_on] == 'warning',
    includeSuggestions: true,
    files: files,
  }
end

def post_review(options, payload)
  uri = URI.join(options[:api_url].end_with?('/') ? options[:api_url] : "#{options[:api_url]}/", 'reviews/pr')
  request = Net::HTTP::Post.new(uri)
  request['Content-Type'] = 'application/json'
  request['Authorization'] = "Bearer #{options[:token]}" if options[:token] && !options[:token].empty?
  request.body = JSON.generate(payload)

  response = Net::HTTP.start(uri.hostname, uri.port, use_ssl: uri.scheme == 'https') do |http|
    http.request(request)
  end
  raise "Review API failed: HTTP #{response.code} #{response.body}" unless response.is_a?(Net::HTTPSuccess)

  JSON.parse(response.body)
end

def issue_key(issue)
  [
    issue['location'],
    issue['inputExpression'],
    issue['recommendedExpression'],
    issue['reason'],
  ].join('|')
end

def line_from_location(location)
  return '' if location.nil?

  match = location.match(/:(\d+)$/)
  match ? match[1] : ''
end

def markdown_report(result)
  issues = result.fetch('issues', []).uniq { |issue| issue_key(issue) }
  summary = result.fetch('summary', {})
  lines = []
  lines << '<!-- aulms-artifact-review -->'
  lines << '## 데이터 사전 표준 검증 결과'
  lines << ''
  lines << "| Severity | Count |"
  lines << "|---|---:|"
  lines << "| ERROR | #{summary.fetch('errorCount', 0)} |"
  lines << "| WARNING | #{summary.fetch('warningCount', 0)} |"
  lines << "| INFO | #{summary.fetch('infoCount', 0)} |"
  lines << ''
  if issues.empty?
    lines << '표준 위반 없음.'
  else
    lines << '### 위반 목록'
    lines << ''
    lines << '| File | Line | Severity | Input | Recommendation | Reason |'
    lines << '|---|---:|---|---|---|---|'
    issues.each do |issue|
      location = issue['location'].to_s
      file = location.sub(/:\d+$/, '')
      lines << [
        file,
        line_from_location(location),
        issue['severity'],
        issue['inputExpression'],
        issue['recommendedExpression'],
        issue['reason'],
      ].map { |value| value.to_s.gsub('|', '\\|') }.join(' | ').prepend('| ').concat(' |')
    end
  end
  lines << ''
  lines << "Exit code: `#{result.fetch('exitCode', 0)}`"
  lines << ''
  lines.join("\n")
end

begin
  result = if options[:response_json]
             JSON.parse(File.read(options[:response_json]))
           else
             payload = build_request(options)
             post_review(options, payload)
           end

  ensure_parent(options[:output_json])
  ensure_parent(options[:output_md])
  File.write(options[:output_json], JSON.pretty_generate(result))
  File.write(options[:output_md], markdown_report(result))

  exit(result.fetch('exitCode', 0).to_i)
rescue StandardError => e
  warn e.message
  exit 4
end
