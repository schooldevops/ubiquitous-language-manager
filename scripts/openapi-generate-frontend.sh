#!/usr/bin/env sh
set -eu

openapi-generator-cli generate \
  -g typescript-axios \
  -i openapi/openapi.yaml \
  -o generated/frontend \
  --additional-properties=npmName=@aulms/api-client,supportsES6=true,withSeparateModelsAndApi=true,apiPackage=api,modelPackage=model

