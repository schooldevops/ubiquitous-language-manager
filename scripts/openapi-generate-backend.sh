#!/usr/bin/env sh
set -eu

openapi-generator-cli generate \
  -g kotlin-spring \
  -i openapi/openapi.yaml \
  -o generated/backend \
  --additional-properties=basePackage=com.aulms,apiPackage=com.aulms.api,modelPackage=com.aulms.model,configPackage=com.aulms.configuration,useSpringBoot3=true,interfaceOnly=true,useTags=true,gradleBuildFile=false,exceptionHandler=false,documentationProvider=none,enumPropertyNaming=original

