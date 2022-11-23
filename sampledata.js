// sample data for requests

// PRODUCT
[
    {
      "brandId": "ad0123",
      "clientSkuId": "srssc",
      "description": "red shirt for men",
      "mrp": 3999,
      "name": "small red shirt summer collection"
    },
    {
      "brandId": "ad0124",
      "clientSkuId": "mrssc",
      "description": "red shirt for men",
      "mrp": 4099,
      "name": "medium red shirt summer collection"
    },
    {
      "brandId": "ad0125",
      "clientSkuId": "lwsn",
      "description": "light weight sneakers for men",
      "mrp": 4499,
      "name": "light weight sneakers"
    }
]

// CHANNEL LISTING
{
    "channelId": 10000002,
    "channelListingFormList": [
      {
        "channelSkuId": "fadrs",
        "clientSkuId": "srssc"
      }
    ],
    "clientId": 10000001
  }

// create order
{
    "channelId": 10000002,
    "channelOrderId": "string2",
    "channelOrderItems": [
      {
        "channelSkuId": "fadrs",
        "orderedQuantity": 4,
        "sellingPricePerUnit": 2341
      }
    ],
    "clientId": 10000001,
    "customerId": 10000152
  }

//  bin-sku
[
    {
      "binId": 10000001,
      "clientSkuId": "srssc",
      "quantity": 5
    },
    {
      "binId": 10000002,
      "clientSkuId": "mrssc",
      "quantity": 7
    }
  ]