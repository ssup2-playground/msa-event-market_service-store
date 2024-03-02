var api_spec =
{
  "openapi": "3.0.1",
  "info": {
    "title": "msa-event-market store service",
    "description": "msa-event-market store service",
    "version": "1.0.0"
  },
  "servers": [
    {
      "url": "http:://msa-event-market.com",
      "description": "msa-event-market store service"
    }
  ],
  "tags": [
    {
      "name": "Store"
    },
    {
      "name": "Product"
    }
  ],
  "paths": {
    "/v1/stores/{storeId}": {
      "get": {
        "tags": [
          "Store"
        ],
        "operationId": "getStore",
        "parameters": [
          {
            "name": "storeId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/StoreInfoRes"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "Store"
        ],
        "operationId": "updateStore",
        "parameters": [
          {
            "name": "storeId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/StoreInfoReq"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK"
          }
        },
        "security": [
          {
            "AccessToken": []
          }
        ]
      },
      "delete": {
        "tags": [
          "Store"
        ],
        "operationId": "deleteStore",
        "parameters": [
          {
            "name": "storeId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK"
          }
        },
        "security": [
          {
            "AccessToken": []
          }
        ]
      }
    },
    "/v1/stores/{storeId}/products/{productId}": {
      "get": {
        "tags": [
          "Product"
        ],
        "operationId": "getProduct",
        "parameters": [
          {
            "name": "storeId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          },
          {
            "name": "productId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/ProductInfoRes"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "Product"
        ],
        "operationId": "updateProduct",
        "parameters": [
          {
            "name": "storeId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          },
          {
            "name": "productId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ProductInfoReq"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK"
          }
        },
        "security": [
          {
            "AccessToken": []
          }
        ]
      },
      "delete": {
        "tags": [
          "Product"
        ],
        "operationId": "deleteProduct",
        "parameters": [
          {
            "name": "storeId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          },
          {
            "name": "productId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK"
          }
        },
        "security": [
          {
            "AccessToken": []
          }
        ]
      }
    },
    "/v1/stores": {
      "get": {
        "tags": [
          "Store"
        ],
        "operationId": "listStore",
        "parameters": [
          {
            "name": "offset",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 0
            }
          },
          {
            "name": "limit",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 50
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/StoreInfoListRes"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Store"
        ],
        "operationId": "createStore",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/StoreInfoReq"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/StoreInfoRes"
                }
              }
            }
          }
        },
        "security": [
          {
            "AccessToken": []
          }
        ]
      }
    },
    "/v1/stores/{storeId}/products": {
      "get": {
        "tags": [
          "Product"
        ],
        "operationId": "listProduct",
        "parameters": [
          {
            "name": "storeId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          },
          {
            "name": "offset",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 0
            }
          },
          {
            "name": "limit",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 50
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/ProductInfoListRes"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Product"
        ],
        "operationId": "createProduct",
        "parameters": [
          {
            "name": "storeId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ProductInfoReq"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/ProductInfoRes"
                }
              }
            }
          }
        },
        "security": [
          {
            "AccessToken": []
          }
        ]
      }
    },
    "/v1/stores/{storeId}/products/{productId}/quantity/increase": {
      "post": {
        "tags": [
          "Product"
        ],
        "operationId": "increaseProductQuantity",
        "parameters": [
          {
            "name": "storeId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          },
          {
            "name": "productId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ProductQuantityReq"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/ProductQuantityRes"
                }
              }
            }
          }
        },
        "security": [
          {
            "AccessToken": []
          }
        ]
      }
    },
    "/v1/stores/{storeId}/products/{productId}/quantity/decrease": {
      "post": {
        "tags": [
          "Product"
        ],
        "operationId": "decreaseProductQuantity",
        "parameters": [
          {
            "name": "storeId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          },
          {
            "name": "productId",
            "in": "path",
            "required": true,
            "schema": {
              "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ProductQuantityReq"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/ProductQuantityRes"
                }
              }
            }
          }
        },
        "security": [
          {
            "AccessToken": []
          }
        ]
      }
    }
  },
  "components": {
    "schemas": {
      "StoreInfoReq": {
        "required": [
          "description",
          "name"
        ],
        "type": "object",
        "properties": {
          "name": {
            "maxLength": 50,
            "minLength": 0,
            "type": "string"
          },
          "description": {
            "maxLength": 100,
            "minLength": 0,
            "type": "string"
          }
        }
      },
      "ProductInfoReq": {
        "required": [
          "description",
          "name"
        ],
        "type": "object",
        "properties": {
          "name": {
            "maxLength": 50,
            "minLength": 0,
            "type": "string"
          },
          "description": {
            "maxLength": 100,
            "minLength": 0,
            "type": "string"
          },
          "quantity": {
            "minimum": 0,
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "StoreInfoRes": {
        "required": [
          "description",
          "name"
        ],
        "type": "object",
        "properties": {
          "id": {
            "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
            "type": "string"
          },
          "name": {
            "type": "string"
          },
          "description": {
            "type": "string"
          }
        }
      },
      "ProductInfoRes": {
        "required": [
          "description",
          "name"
        ],
        "type": "object",
        "properties": {
          "id": {
            "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
            "type": "string"
          },
          "storeId": {
            "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
            "type": "string"
          },
          "name": {
            "maxLength": 50,
            "minLength": 0,
            "type": "string"
          },
          "description": {
            "maxLength": 100,
            "minLength": 0,
            "type": "string"
          },
          "quantity": {
            "minimum": 0,
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "ProductQuantityReq": {
        "type": "object",
        "properties": {
          "quantity": {
            "minimum": 0,
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "ProductQuantityRes": {
        "type": "object",
        "properties": {
          "id": {
            "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
            "type": "string"
          },
          "quantity": {
            "minimum": 0,
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "StoreInfoListRes": {
        "type": "object",
        "properties": {
          "stores": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/StoreInfoRes"
            }
          }
        }
      },
      "ProductInfo": {
        "type": "object",
        "properties": {
          "id": {
            "type": "string",
            "format": "uuid"
          },
          "storeId": {
            "type": "string",
            "format": "uuid"
          },
          "name": {
            "type": "string"
          },
          "description": {
            "type": "string"
          },
          "quantity": {
            "minimum": 0,
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "ProductInfoListRes": {
        "type": "object",
        "properties": {
          "products": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/ProductInfo"
            }
          }
        }
      }
    },
    "securitySchemes": {
      "AccessToken": {
        "type": "http",
        "name": "AccessToken",
        "scheme": "bearer",
        "bearerFormat": "JWT"
      }
    }
  }
}
