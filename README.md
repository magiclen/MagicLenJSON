Mson
=================================

# Introduction

**Mson** (also called **MagicLenJSON**) is a Java library which includes [Gson](https://github.com/google/gson "Gson") library and [json.org](http://www.json.org/java/index.html "json.org") library to do some json-related operations. My goal is to convert JSON data formats easily among JSON formatted strings, any objects, and XML data formats.

# Usage

## Mson Class

**Mson** class is in the *org.magiclen.mson* package.

All the **JSONObject** class and **JSONArray** class used in **Mson** class are in the *org.magiclen.json* package from [json.org](http://www.json.org/java/index.html "json.org") library. Both of the two classes are modified to extend a **JSONType** abstract class, which is in the *org.magiclen.mson* package.

### Initialize

You don't need to do initialize when you use **Mson** class. Just use its static methods to do what you want.

### Format a JSON to string

You can use **toString** method to format a **JSONType** to a string with a pretty form.

For example,

    final org.magiclen.json.JSONObject authorLen = new org.magiclen.json.JSONObject();
    authorLen.put("id", 1);
    authorLen.put("name", "Magic Len");
    authorLen.put("lang", "繁體中文");
    authorLen.put("phone", new org.magiclen.json.JSONArray("[\"88693929304\", \"041112233\"]"));
    authorLen.put("birthday", new org.magiclen.json.JSONArray("[1993, 8, 10]"));
    final org.magiclen.json.JSONObject authorDean = new org.magiclen.json.JSONObject();
    authorDean.put("id", 2);
    authorDean.put("name", "Dean ML");
    authorDean.put("lang", "English");
    authorDean.put("phones", new String[]{"711423000", "556432234"});
    authorDean.put("birthday", new int[]{1991, 1, 1});
    final org.magiclen.json.JSONArray authors = new org.magiclen.json.JSONArray();
    authors.put(authorLen);
    authors.put(authorDean);
    final org.magiclen.json.JSONObject websiteHeader = new org.magiclen.json.JSONObject();
    websiteHeader.put("website", "http://magiclen.org");
    websiteHeader.put("authors", authors);

Now, we have a **JSONObject** named websiteHeader, and we want to format it to a string. We can write code like this:

    final String jsonString = org.magiclen.mson.Mson.toString(websiteHeader, true);
    System.out.println(jsonString);

The result is,

    {
        "website" : "http://magiclen.org",
        "authors" : [
            {
                "birthday" : [
                    1993,
                    8,
                    10
                ],
                "phone" : [
                    "88693929304",
                    "041112233"
                ],
                "name" : "Magic Len",
                "id" : 1,
                "lang" : "繁體中文"
            },
            {
                "birthday" : [
                    1991,
                    1,
                    1
                ],
                "name" : "Dean ML",
                "phones" : [
                    "711423000",
                    "556432234"
                ],
                "id" : 2,
                "lang" : "English"
            }
        ]
    }

What's the different? If you use **toString** method in **JSONObject** directly, you will get this result:

    {"website":"http://magiclen.org","authors":[{"birthday":[1993,8,10],"phone":[88693929304,"041112233"],"name":"Magic Len","id":1,"lang":"繁體中文"},{"birthday":[1991,1,1],"name":"Dean ML","phones":["711423000","556432234"],"id":2,"lang":"English"}]}

See? If you didn't use **Mson** to format. The string is not instinctive very much. You can also use **Gson** to do the same thing, but it is a little harder than **Mson**.

If the **JSONType** is too big, it will take a long time and big memory to format it to a string. The **toString** method in **Mson** class allows you to write characters to any output stream such as **FileOutputStream**. We suggest you use it asynchronously.

We reuse the **JSONObject** named websiteHeader as above for instance,

    new Thread(() -> {
        try {
            final FileOutputStream fos = new FileOutputStream(new File("/home/magiclen/json.js"));
            Mson.toString(fos, websiteHeader, (object, enter, depth) -> {
                // Do something
            });
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }).start();


### Convert any object to JSON

You can use **toJSON** method to convert any object even Array to **JSONType**.

For example,

    final int[] array = new int[]{100, 200, 300, 400};
    final org.magiclen.mson.JSONType json = Mson.toJSON(array);
    System.out.println(Mson.format(json, true));

The result is,

    [
        100,
        200,
        300,
        400
    ]

For example,

    class MyClass {
        String a;
        int b;
        double c;

        @Override
        public String toString() {
            return String.format("a=%s b=%d c=%f", a, b, c);
        }
    }
    ...
    final MyClass obj = new MyClass();
    obj.a = "hello";
    obj.b = 888;
    obj.c = 3.14;
    final org.magiclen.mson.JSONType json = Mson.toJSON(obj);
    System.out.println(Mson.toString(json, true));

The result is,

    {
        "a" : "hello",
        "b" : 888,
        "c" : 3.14
    }

If you are sure whether the object is array or not, you can assign **OBJECT** type or **ARRAY** type defined in *org.magiclen.mson.JSONType.Type* explicitly to improve the performance. The code can be modified like this:

    final org.magiclen.json.JSONObject json = (org.magiclen.json.JSONObject) Mson.toJSON(obj, org.magiclen.mson.JSONType.Type.OBJECT);

### Convert JSON to object

You can use **toObject** or **toArray** method to convert **JSONType** to objects in your candidate list.

We reuse **MyClass** in previous paragraph and use **toObject** for example,

    final org.magiclen.json.JSONObject json = (org.magiclen.json.JSONObject) Mson.fromString("{\"a\":\"hello\",\"b\":888,\"c\":3.14}", JSONType.Type.OBJECT);
    final Object obj = Mson.toObject(json, String.class, MyClass.class);
    if (obj instanceof MyClass) {
        System.out.println(((MyClass) obj).a);
    }

The result is,

    hello

Use **toArray** for example,

    final org.magiclen.json.JSONArray json = (org.magiclen.json.JSONArray) Mson.fromString("[\"hello\",888,3.14,{\"a\":\"hello\",\"b\":888,\"c\":3.14}]", JSONType.Type.ARRAY);
    final Object[] array = Mson.toArray(json, MyClass.class);
    for (final Object obj : array) {
        System.out.println(obj);
    }

The result is,

    hello
    888
    3.14
    a=hello b=888 c=3.140000

### Convert JSON to XML

You can use **toXML** method to convert **JSONType** to XML data formats.

For example,

    final org.magiclen.json.JSONObject json = (org.magiclen.json.JSONObject) Mson.fromString("{\"a\":\"hello\",\"b\":888,\"c\":3.14}", JSONType.Type.OBJECT);
    System.out.println(Mson.toXML(json));

The result is,


    <a>hello</a><b>888</b><c>3.14</c>

### Convert XML to JSON

You can use **fromXML** method to convert XML data formats to **JSONObject**.

For example,

    System.out.println(Mson.fromXML("<p>hello</p><p>888</p><p>3.14</p><a href=\"#\">hello</a><b>888</b><c>3.14</c>"));

The result is,

     {"root":{"p":["hello",888,3.14],"a":{"href":"#","content":"hello"},"b":888,"c":3.14}}

### Sort JSONArray

You can use **sort** method to sort a **JSONArray** object.

For example,

    final org.magiclen.json.JSONArray array = (org.magiclen.json.JSONArray) Mson.fromString("[5, 4, 3, 2, 1]", JSONType.Type.ARRAY);
    Mson.sort(array, new Comparator(){
      @Override
      public int compare(final Object o1, final Object o2) {
          return (int)o1 - (int)o2;
      }
    });
    System.out.println(array.toString());

The result is,

    [1,2,3,4,5]

## JSONType Class

**JSONType** class is in the *org.magiclen.mson* package. It is a abstract super class of **JSONObject** class and **JSONArray** class which are in the *org.magiclen.json* package.

**JSONType** class has some implemented methods to do some json-related operations.

    jsonType.toString(true);

equals to

    Mson.toString(jsonType, true);

This code,

    jsonType.toXML();

equals to

    Mson.toXML(jsonType);

This code,

    jsonObject.toObject(type);

equals to

    Mson.toObject(jsonObject, type);

This code,

    jsonArray.toObject(type);

equals to

    Mson.toArray(jsonObject, type);

# License

    Copyright 2015-2017 magiclen.org

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

# What's More?

Please check out our web page at

https://magiclen.org/mson/
