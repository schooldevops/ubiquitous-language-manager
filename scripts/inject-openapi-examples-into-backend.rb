#!/usr/bin/env ruby
# frozen_string_literal: true

require 'json'
require 'yaml'

ROOT = File.expand_path('..', __dir__)
OPENAPI_FILE = File.join(ROOT, 'openapi/openapi.yaml')
GENERATED_API_DIR = File.join(ROOT, 'generated/backend/src/main/kotlin/com/aulms/api')

def load_yaml(path)
  YAML.load_file(path)
end

def resolve_ref(ref)
  file_part, fragment = ref.split('#', 2)
  path =
    if file_part.end_with?('/openapi.yaml') || file_part == 'openapi.yaml'
      OPENAPI_FILE
    else
      File.expand_path(file_part, File.dirname(OPENAPI_FILE))
    end
  node = load_yaml(path)
  fragment.to_s.sub(%r{^/}, '').split('/').reject(&:empty?).each do |part|
    node = node.fetch(part)
  end
  node
end

def resolved(node)
  return node unless node.is_a?(Hash) && node.key?('$ref')

  resolve_ref(node.fetch('$ref'))
end

def content_examples(content)
  return [] unless content.is_a?(Hash)

  examples = content['examples']
  return [] unless examples.is_a?(Hash)

  examples.map do |name, example|
    example = example || {}
    {
      name: name,
      summary: example['summary'],
      value: example.key?('value') ? example['value'] : example
    }
  end
end

def kotlin_string(value)
  value.to_s.dump
end

def example_value(value)
  if value.is_a?(Hash) || value.is_a?(Array)
    JSON.pretty_generate(value).dump
  else
    value.to_s.dump
  end
end

def example_objects(examples, indent)
  lines = examples.map do |example|
    args = [
      "name = #{kotlin_string(example.fetch(:name))}",
      ("summary = #{kotlin_string(example[:summary])}" if example[:summary]),
      "value = #{example_value(example.fetch(:value))}"
    ].compact
    "#{indent}ExampleObject(#{args.join(', ')})"
  end
  "[\n#{lines.join(",\n")}\n#{indent[0...-4]}]"
end

def request_body_annotation(examples)
  <<~KOTLIN.rstrip
    io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = [Content(
                    mediaType = "application/json",
                    examples = #{example_objects(examples, '                    ')}
                )]
            )
  KOTLIN
end

def collect_operation_examples
  root = load_yaml(OPENAPI_FILE)
  operations = {}

  root.fetch('paths').each_value do |path_ref|
    path_item = resolved(path_ref)
    next unless path_item.is_a?(Hash)

    path_item.each do |method, operation|
      next unless %w[get post put patch delete].include?(method)

      operation_id = operation['operationId']
      next unless operation_id

      request_examples = content_examples(operation.dig('requestBody', 'content', 'application/json'))

      response_examples = {}
      operation.fetch('responses', {}).each do |status, response|
        response = resolved(response)
        examples = content_examples(response.dig('content', 'application/json'))
        response_examples[status] = examples unless examples.empty?
      end

      operations[operation_id] = {
        request: request_examples,
        responses: response_examples
      }
    end
  end

  operations
end

def inject_request_body(block, examples)
  return block if examples.empty?
  return block if block.include?('requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody')

  block.sub(/(\n\s*responses = \[)/) do
    "\n        requestBody = #{request_body_annotation(examples)},#{$1}"
  end
end

def inject_response_examples(block, response_examples)
  response_examples.reduce(block) do |current, (status, examples)|
    current.gsub(/ApiResponse\(responseCode = "#{Regexp.escape(status)}", description = "([^"]*)", content = \[Content\((.*)\)\]\)(,?)/) do
      description = Regexp.last_match(1)
      content_args = Regexp.last_match(2)
      trailing_comma = Regexp.last_match(3)
      next Regexp.last_match(0) if content_args.include?('examples =')

      examples_code = example_objects(examples, '                ')
      "ApiResponse(responseCode = \"#{status}\", description = \"#{description}\", content = [Content(#{content_args}, examples = #{examples_code})])#{trailing_comma}"
    end
  end
end

def inject_operation_examples(content, operation_id, examples)
  content.gsub(/@Operation\(\n(?:(?!\n\s*@Operation\().)*?operationId = "#{Regexp.escape(operation_id)}",(?:(?!\n\s*@Operation\().)*?\n\s*\)\n\s*@RequestMapping/m) do |block|
    request_mapping_tail = block[/\n\s*\)\n\s*@RequestMapping\z/]
    operation_block = block.sub(/\n\s*\)\n\s*@RequestMapping\z/, '')
    operation_block = inject_request_body(operation_block, examples.fetch(:request))
    operation_block = inject_response_examples(operation_block, examples.fetch(:responses))
    "#{operation_block}#{request_mapping_tail}"
  end
end

operations = collect_operation_examples

Dir[File.join(GENERATED_API_DIR, '*Api.kt')].each do |file|
  content = File.read(file)
  updated = operations.reduce(content) do |current, (operation_id, examples)|
    inject_operation_examples(current, operation_id, examples)
  end
  File.write(file, updated) if updated != content
end

puts "Injected OpenAPI examples into generated backend API annotations."
