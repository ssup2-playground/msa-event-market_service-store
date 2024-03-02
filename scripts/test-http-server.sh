#!/bin/bash
# -- Variables --
EXIT_CODE=0
ADMIN_ACCESS_TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjc5NDg3NjA0NjcsImlhdCI6MTY0MTU2MDQ2NywiVXNlcklEIjoiMjlkYjJmMjQtMWU3OC00ODdkLTk3N2MtYjFjYWYyMGEzNzE4IiwiVXNlckxvZ2luSUQiOiJhZG1pbjEyMzQiLCJVc2VyUm9sZSI6ImFkbWluIn0.-dUyOkJBtldz3ONRFhV766ma-i-v2IvSW27Z5vQZu-E"
USER_ACCESS_TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjc5NDg3NjA0MzQsImlhdCI6MTY0MTU2MDQzNCwiVXNlcklEIjoiMzk3ODhlY2UtYjQ1NC00ZTk0LWJkODktNDIzYzYzYWNhYmFiIiwiVXNlckxvZ2luSUQiOiJ1c2VyMTIzNCIsIlVzZXJSb2xlIjoidXNlciJ9.NlI4A8WRt-YlYPEC4v0bZucaKyiRzacqn8hpG-wOu20"

STORE_ID=""
STORE_NAME="mystore"
STORE_DESCRIPTION="mydescription"
STORE_DESCRIPTION2="mydescription2"

PRODUCT_ID=""
PRODUCT_NAME="myproduct"
PRODUCT_DESCRIPTION="mydescription"
PRODUCT_DESCRIPTION2="mydescription2"
PRODUCT_QUANTITY="10"
PRODUCT_QUANTITY_INCDEC="5"
PRODUCT_QUANTITY_RESULT="15"

# -- Test cases --
## Create store
echo "-- Create store start --"
RESPONSE=$(curl --no-progress-meter --write-out '\n%{http_code}' \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN" \
  -d "{\"name\": \"$STORE_NAME\", \"description\": \"$STORE_DESCRIPTION\"}" \
  http://localhost/v1/stores)
RESPONSE_HTTP_CODE=$(tail -n1 <<< "$RESPONSE")
RESPONSE_BODY=$(sed '$ d' <<< "$RESPONSE")
echo Response HTTP Code : $RESPONSE_HTTP_CODE
echo Response Body : $RESPONSE_BODY
if [ $RESPONSE_HTTP_CODE != "200" ]; then
  EXIT_CODE=1
else
  STORE_ID=$(jq -r .id <<< "$RESPONSE_BODY")
fi
echo "-- Create store end --"

## Update store
echo "-- Update store start --"
RESPONSE=$(curl --no-progress-meter --write-out '\n%{http_code}' \
  -X PUT \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN" \
  -d "{\"name\": \"$STORE_NAME\", \"description\": \"$STORE_DESCRIPTION2\"}" \
  http://localhost/v1/stores/$STORE_ID)
RESPONSE_HTTP_CODE=$(tail -n1 <<< "$RESPONSE")
echo Response HTTP Code : $RESPONSE_HTTP_CODE
if [ $RESPONSE_HTTP_CODE != "200" ]; then
  EXIT_CODE=1
fi
echo "-- Update store end --"

## Get store
echo "-- Get store start --"
RESPONSE=$(curl --no-progress-meter --write-out '\n%{http_code}' \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN" \
  http://localhost/v1/stores/$STORE_ID)
RESPONSE_HTTP_CODE=$(tail -n1 <<< "$RESPONSE")
RESPONSE_BODY=$(sed '$ d' <<< "$RESPONSE")
echo Response HTTP Code : $RESPONSE_HTTP_CODE
echo Response Body : $RESPONSE_BODY
if [ $RESPONSE_HTTP_CODE != "200" ]; then
  EXIT_CODE=1
else
  STORE_RESPONSE_DESCRIPTION=$(jq -r .description <<< "$RESPONSE_BODY")
  if [ $STORE_RESPONSE_DESCRIPTION != $STORE_DESCRIPTION2 ]; then
    echo "!! Store description info is diff !!"
    EXIT_CODE=1
  fi
fi
echo "-- Get store end --"

## Create product
echo "-- Create product start --"
RESPONSE=$(curl --no-progress-meter --write-out '\n%{http_code}' \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN" \
  -d "{\"name\": \"$PRODUCT_NAME\", \"description\": \"$PRODUCT_DESCRIPTION\", \"quantity\": $PRODUCT_QUANTITY}" \
  http://localhost/v1/stores/$STORE_ID/products)
RESPONSE_HTTP_CODE=$(tail -n1 <<< "$RESPONSE")
RESPONSE_BODY=$(sed '$ d' <<< "$RESPONSE")
echo Response HTTP Code : $RESPONSE_HTTP_CODE
echo Response Body : $RESPONSE_BODY
if [ $RESPONSE_HTTP_CODE != "200" ]; then
  EXIT_CODE=1
else
  PRODUCT_ID=$(jq -r .id <<< "$RESPONSE_BODY")
fi
echo "-- Create product end --"

## Update product
echo "-- Update product start --"
RESPONSE=$(curl --no-progress-meter --write-out '\n%{http_code}' \
  -X PUT \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN" \
  -d "{\"name\": \"$PRODUCT_NAME\", \"description\": \"$PRODUCT_DESCRIPTION2\", \"quantity\": $PRODUCT_QUANTITY}" \
  http://localhost/v1/stores/$STORE_ID/products/$PRODUCT_ID)
RESPONSE_HTTP_CODE=$(tail -n1 <<< "$RESPONSE")
echo Response HTTP Code : $RESPONSE_HTTP_CODE
if [ $RESPONSE_HTTP_CODE != "200" ]; then
  EXIT_CODE=1
fi
echo "-- Update product end --"

## Update product with user token / Fail
echo "-- Update product with user token start --"
RESPONSE=$(curl --no-progress-meter --write-out '\n%{http_code}' \
  -X PUT \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $USER_ACCESS_TOKEN" \
  -d "{\"name\": \"$PRODUCT_NAME\", \"description\": \"$PRODUCT_DESCRIPTION2\", \"quantity\": $PRODUCT_QUANTITY}" \
  http://localhost/v1/stores/$STORE_ID/products/$PRODUCT_ID)
RESPONSE_HTTP_CODE=$(tail -n1 <<< "$RESPONSE")
echo Response HTTP Code : $RESPONSE_HTTP_CODE
if [ $RESPONSE_HTTP_CODE != "403" ]; then
  EXIT_CODE=1
fi
echo "-- Update product with user token end --"

## Get product
echo "-- Get product start --"
RESPONSE=$(curl --no-progress-meter --write-out '\n%{http_code}' \
  -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN" \
  http://localhost/v1/stores/$STORE_ID/products/$PRODUCT_ID)
RESPONSE_HTTP_CODE=$(tail -n1 <<< "$RESPONSE")
RESPONSE_BODY=$(sed '$ d' <<< "$RESPONSE")
echo Response HTTP Code : $RESPONSE_HTTP_CODE
echo Response Body : $RESPONSE_BODY
if [ $RESPONSE_HTTP_CODE != "200" ]; then
  EXIT_CODE=1
else
  PRODUCT_RESPONSE_DESCRIPTION=$(jq -r .description <<< "$RESPONSE_BODY")
  if [ $PRODUCT_RESPONSE_DESCRIPTION != $PRODUCT_DESCRIPTION2 ]; then
    echo "!! Product description info is diff !!"
    EXIT_CODE=1
  fi
fi
echo "-- Get product end --"

## Increase product
echo "-- Increase product start --"
RESPONSE=$(curl --no-progress-meter --write-out '\n%{http_code}' \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN" \
  -d "{\"quantity\": $PRODUCT_QUANTITY_INCDEC}" \
  http://localhost/v1/stores/$STORE_ID/products/$PRODUCT_ID/quantity/increase)
RESPONSE_HTTP_CODE=$(tail -n1 <<< "$RESPONSE")
RESPONSE_BODY=$(sed '$ d' <<< "$RESPONSE")
echo Response HTTP Code : $RESPONSE_HTTP_CODE
echo Response Body : $RESPONSE_BODY
if [ $RESPONSE_HTTP_CODE != "200" ]; then
  EXIT_CODE=1
else
  PRODUCT_RESPONSE_QUANTITY=$(jq -r .quantity <<< "$RESPONSE_BODY")
  if [ $PRODUCT_RESPONSE_QUANTITY != $PRODUCT_QUANTITY_RESULT ]; then
    echo "!! Product quantity is diff !!"
    EXIT_CODE=1
  fi
fi
echo "-- Increase product end --"

## Decrease product
echo "-- Decrease product start --"
RESPONSE=$(curl --no-progress-meter --write-out '\n%{http_code}' \
  -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN" \
  -d "{\"quantity\": $PRODUCT_QUANTITY_INCDEC}" \
  http://localhost/v1/stores/$STORE_ID/products/$PRODUCT_ID/quantity/decrease)
RESPONSE_HTTP_CODE=$(tail -n1 <<< "$RESPONSE")
RESPONSE_BODY=$(sed '$ d' <<< "$RESPONSE")
echo Response HTTP Code : $RESPONSE_HTTP_CODE
echo Response Body : $RESPONSE_BODY
if [ $RESPONSE_HTTP_CODE != "200" ]; then
  EXIT_CODE=1
else
  PRODUCT_RESPONSE_QUANTITY=$(jq -r .quantity <<< "$RESPONSE_BODY")
  if [ $PRODUCT_RESPONSE_QUANTITY != $PRODUCT_QUANTITY ]; then
    echo "!! Product quantity is diff !!"
    EXIT_CODE=1
  fi
fi
echo "-- Decrease product end --"

## Delete product
echo "-- Delete product start --"
RESPONSE=$(curl --no-progress-meter --write-out '\n%{http_code}' \
  -X DELETE \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN" \
  http://localhost/v1/stores/$STORE_ID/products/$PRODUCT_ID)
RESPONSE_HTTP_CODE=$(tail -n1 <<< "$RESPONSE")
echo Response HTTP Code : $RESPONSE_HTTP_CODE
if [ $RESPONSE_HTTP_CODE != "200" ]; then
  EXIT_CODE=1
fi
echo "-- Delete product end --"

## Delete store
echo "-- Delete store start --"
RESPONSE=$(curl --no-progress-meter --write-out '\n%{http_code}' \
  -X DELETE \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN" \
  http://localhost/v1/stores/$STORE_ID)
RESPONSE_HTTP_CODE=$(tail -n1 <<< "$RESPONSE")
echo Response HTTP Code : $RESPONSE_HTTP_CODE
if [ $RESPONSE_HTTP_CODE != "200" ]; then
  EXIT_CODE=1
fi
echo "-- Delete store end --"

# -- Exit --
exit $EXIT_CODE