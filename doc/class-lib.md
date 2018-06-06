本文总结、摘录自书籍[《Java 8 函数式编程》](https://www.amazon.cn/dp/B00VDSW7AE/ref=sr_1_1?s=books&ie=UTF8&qid=1528162560&sr=1-1&keywords=java+8%E5%87%BD%E6%95%B0%E5%BC%8F%E7%BC%96%E7%A8%8B)

## 类库

### 基本类型

在 Java 中，有一些相伴的类型，比如 int 和 Integer—— 前者是基本类型，后者是装箱类型。基本类型内建在语言和运行环境中，是基本的程序构建模块；而装箱类型属于普通的 Java 类，只不过是对基本类型的一种封装。 Java 的泛型是基于对泛型参数类型的擦除。这就解释了为什么在 Java 中想要一个包含整型值的列表 `List<int>`是不可行的，泛型只接收对象类型。

麻烦的是，由于装箱类型是对象，因此在内存中存在额外开销。比如，整型在内存中占用 4 字节，整型对象却要占用 16 字节。这一情况在数组上更加严重，整型数组中的每个元素 只占用基本类型的内存，而整型对象数组中，每个元素都是内存中的一个指针，指向 Java 堆中的某个对象。在最坏的情况下，同样大小的数组，Integer[] 要比 int[] 多占用 6 倍内存。

将基本类型转换为装箱类型，称为装箱，反之拆箱。两者都需要额外的开销，对于需要大量数值运算的算法来说，装箱和拆箱的计算开销，以及装箱类型占用的额外内 存，会明显减缓程序的运行速度。为了减小这些性能开销，Stream 类的某些方法对基本类型和装箱类型做了区分。如`mapTolong`函数与其它类似函数即为该方面的一个尝试。在 Java 8 中，仅对整型、 长整型和双浮点型做了特殊处理，因为它们在数值计算中用得最多，特殊处理后的系统性 能提升效果最明显。

在有可能的情况下，我们应该尽量多的使用对基本类型做过特殊处理的方法，进而改善性能：

```java
List<Integer> items = new ArrayList<>();
items.add(12);
items.add(13);

int[] array = items.stream().mapToInt(item -> item).toArray();
```

### 重载解析

在 Java 中可以重载方法，造成多个方法有相同的方法名，但签名确不一样。这在推断参数 类型时会带来问题，因为系统可能会推断出多种类型。这时，javac 会挑出最具体的类型。下面例子中执行的`add`方法为第一个。

```java
public void add(String item) {...}

public void add(Object item) {...}
```

```java
add("aaa");
```

Lambda 重载方法：

```java
public interface BiFunction<T, R> {
    R test(T t);
}

public interface IntegerFunction extends BiFunction<Integer, Integer> {
    //
}
```

```java
public void add (BiFunction<Integer, Integer> lambda) {...}

public void add (IntegerFunction lambda) {...}
```

```java
add ((x) -> x + 1);
```

Java也会推到出最具体的函数接口类型，所以执行第二个方法。

模糊调用错误：

```java
public interface BiFunction<T, R> {
    R test(T t);
}

public interface IntegerFunction {
    Integer test(Integer t);
}
```

```java
add ((x) -> x + 1);
```

在类型匹配上，两个`add`方法都匹配，这回使得代码无法通过编译。

### @FunctionalInterface

该注释会强制 javac 检查一个接口是否符合函数接口的标准。如果该注释添加给一个枚举类型、类或另一个注释，或者接口包含不止一个抽象方法，javac 就会报错。重构代码时， 使用它能很容易发现问题。

### 接口default关键字

我们在代码实现接口的实践中，若遇见接口增加方法定义时，会造成大量的实现类需要实现新增的方法，如`Collection`接口，若此接口新增一个方法，将造成大量的相关实现类必须实现此方法。Java 8 为我们解决了此问题--`default `关键字，使用此关键字修饰的接口方法可以书写代码块，表示方法的默认实现。形式如下：

```java
public interface Say {
    default void say() {
        System.out.println("Hello");
    }
}
```

#### 接口继承

```java
public interface SayThank extends Say {
    @Override
    default void say() {
        System.out.println("Thanks");
    }
}
```

可以使用父接口的方法实现：

```java
public interface SayThank extends Say {
    @Override
    default void say() {
        Say.super.say();
    }
}
```

### 关于多重实现

```java
public interface A {
    default void action() {...}
}

public interface B {
    default void action() {...}
}

public class C implements A, B {
    //
}
```

上面的实现会造成编译错误，因为编译器不明确应该继承哪个接口的默认方法，解决方法就是实现这个方法，实现时可以指定使用接口中的默认实现：

```java
public class C implements A, B {
    public void action() {
        A.super.action();
    }
}
```

### 接口的静态方法

Java 8 中添加的一个新的语言特性，旨在帮助编写类库的开发人员，但对于日常应用程序的开发人员也同样适用。人们在编程过程中积累了这样一条经验，那就是一个包含很多静态方法的类。有时，类是 一个放置工具方法的好地方，比如 Java 7 中引入的 `Objects `类，就包含了很多工具方法， 这些方法不是具体属于某个类的。 当然，如果一个方法有充分的语义原因和某个概念相关，那么就应该将该方法和相关的类 或接口放在一起，而不是放到另一个工具类中。这有助于更好地组织代码，阅读代码的人 也更容易找到相关方法。下面截取`Stream`接口中的静态方法`of` :

```java
@SafeVarargs
@SuppressWarnings("varargs") // Creating a stream from an array is safe
public static<T> Stream<T> of(T... values) {
    return Arrays.stream(values);
}
```

### Optional

`reduce `方法的一个重点尚未提及：`reduce `方法有两种形式，一种如前面出现的需要有一 个初始值，另一种变式则不需要有初始值。没有初始值的情况下，`reduce `的第一步使用 `Stream `中的前两个元素。有时，`reduce `操作不存在有意义的初始值，这样做就是有意义 的，此时，`reduce `方法返回一个 `Optional `对象。 `Optional `是为核心类库新设计的一个数据类型，用来替换 `null `值。人们对原有的 `null `值 有很多抱怨，甚至连发明这一概念的 Tony Hoare 也是如此，他曾说这是自己的一个“价值 连城的错误”。

人们常常使用 `null `值表示值不存在，`Optional `对象能更好地表达这个概念。使用 `null `代 表值不存在的最大问题在于 `NullPointerException`。一旦引用一个存储 `null `值的变量，程 序会立即崩溃。使用 `Optional `对象有两个目的：首先，`Optional `对象鼓励程序员适时检查 变量是否为空，以避免代码缺陷；其次，它将一个类的 API 中可能为空的值文档化，这比 阅读实现代码要简单很多。

`Optional `对象相当于值容器，可以通过`get`方法获取：

```java
Optional<String> apple = Optional.of("apple");
apple.get();
```

`Optional `对象也可以为`null`，因此还有一个对应的工厂方法`empty`，另外一个工厂方法`ofNullable`则可以将`null`值转换成`Optional`对象。

```java
Optional empty = Optional.empty();

Optional empty1 = Optional.ofNullable(null);
```

判断Optional是否为空值：

```java
empty.isPresent();
```

使用`Optional `对象的方式之一是在调用 `get() `方法前，先使用 `isPresent `检查 `Optional `对象是否有值。使用 `orElse `方法则更简洁，当 `Optional `对象为空时，该方法提供了一个 备选值。如果计算备选值在计算上太过繁琐，即可使用 `orElseGet `方法。该方法接受一个 `Supplier `对象，只有在 `Optional `对象真正为空时才会调用。

```java
public void testOptional() {
    Optional<Integer> optionalInteger = Optional.empty();
    System.out.println(optionalInteger.orElse(1));
    System.out.println(optionalInteger.orElseGet(() -> 1));

    Optional<Integer> optionalInteger1 = Optional.of(21);
    System.out.println(optionalInteger1.orElse(21));
    System.out.println(optionalInteger1.orElseGet(() -> 1));
}
```

