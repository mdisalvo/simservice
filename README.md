### simservice: [![Build Status](https://travis-ci.com/mdisalvo/simservice.svg)](https://travis-ci.com/mdisalvo/simservice)

#### What Is This?
A Spring-booted service hosted by Heroku that quickly allows you to determine how similar one item of text is to another.  The text for each item is processed through a chain of tokenizers that vectorize the text into integer maps, which are then used to a compute a similarity score based on Jaccard multi-set indexing.

#### Try It Out:
- Base URL: simservice.herokuapp.com
- [API Documentation](https://simservice.herokuapp.com/swagger-ui.html) 

A single REST endpoint is exposed at ```/docsim``` which accepts a JSON payload that consists of the following:

| Field           | Type         | Description                   |
| --------------- | ------------ | ----------------------------- |
| ```textItemA``` | ```String``` | The text to process for itemA |
| ```textItemB``` | ```String``` | The text to process for itemB |

The response entity:

| Field                     | Type         | Description                                                      |
| ------------------------- | ------------ | ---------------------------------------------------------------- |
| ```message```             | ```String``` | The message describing the results                               |
| ```result```              | ```double``` | The similarity score represented as a double between 0.0 and 1.0 |
| ```md5TextItemA```        | ```String``` | The calculated md5 of the text from itemA                        |
| ```tokenCountTextItemA``` | ```int```    | The token count of the text from itemA                           |
| ```md5TextItemB```        | ```String``` | The calculated md5 of the text from itemB                        |
| ```tokenCountTextItemB``` | ```int```    | The token count of the text from itemB                           |

An example call:
```javascript
// Request
POST simservice.herokuapp.com/docsim
{
    "textItemA": "The quick brown fox jumped over the lazy dog",
    "textItemB": "The quick brown fox jumped over the lazy dog yo"
}
// Response
{
  "message": "Item with md5 08a008a01d498c404b0c30852b39d3b8 is 0.9% similar to item with md5 1086e0074b3fa4f14e214cd99b5b4ca9.",
  "result": 0.9,
  "md5TextItemA": "08a008a01d498c404b0c30852b39d3b8",
  "tokenCountTextItemA": 9,
  "md5TextItemB": "1086e0074b3fa4f14e214cd99b5b4ca9",
  "tokenCountTextItemB": 10
}
```

##### Test It Out:
Want to build and start locally?  Clone the repository, unpack it, and execute the following within the project dir:

```./gradlew bootRun```

This will start a local server on port 8080 to test against.
