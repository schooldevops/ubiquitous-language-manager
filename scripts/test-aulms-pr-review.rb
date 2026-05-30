#!/usr/bin/env ruby
# frozen_string_literal: true

require 'open3'

cmd = [
  'ruby',
  'scripts/aulms-pr-review.rb',
  '--response-json',
  'scripts/fixtures/aulms-pr-review-response.json',
  '--output-json',
  'build/reports/test-aulms-pr-review.json',
  '--output-md',
  'build/reports/test-aulms-pr-review.md',
]

stdout, stderr, status = Open3.capture3(*cmd)
raise "expected exit code 1, got #{status.exitstatus}\n#{stdout}\n#{stderr}" unless status.exitstatus == 1

markdown = File.read('build/reports/test-aulms-pr-review.md')
raise 'missing customerId' unless markdown.include?('customerId')
raise 'missing customerNumber' unless markdown.include?('customerNumber')
raise 'missing CUST_NO' unless markdown.include?('CUST_NO')
raise 'missing duplicate prevention marker' unless markdown.include?('<!-- aulms-artifact-review -->')

puts 'aulms-pr-review e2e ok'
