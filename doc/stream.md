本文总结、摘录自书籍[《Java 8 函数式编程》](https://www.amazon.cn/dp/B00VDSW7AE/ref=sr_1_1?s=books&ie=UTF8&qid=1528162560&sr=1-1&keywords=java+8%E5%87%BD%E6%95%B0%E5%BC%8F%E7%BC%96%E7%A8%8B)

## 流（java.util.stream.Stream）

### 从外部迭代到内部迭代

在处理一个集合时，我们通常会在集合上进行迭代：

```java
Integer total = 0;
for(item ：items) {
    total += item;
}
```

这样写存在的几个问题：

1）每次进行迭代时，都要书写样板代码。

2）将for循环改造成并行方式运行很麻烦，需要修改每个for循环才能实现。

3）无法流畅传达程序员的意图，样板代码模糊了代码的本意。

从背后原理来看，for循环其实是一个封装了迭代的语法糖：

```java
int total = 0;
Iterator<Integer> iterator = items.iterator();
while(iterator.hasNext()) {
    total += iterator.next();
}
```

显示调用对象的hasNext 和next 方法完成迭代，被称为外部迭代。然而外部迭代也存在问题：

1）本质上是一种串行化操作。

另一种方法为内部迭代，内部迭代使用集合的`stream()`方法，如下计算集合中等于2的元素个数：

```java
public void testIterator() {
    List<Integer> items = new ArrayList<>();
    items.add(1);
    items.add(2);
    items.add(2);

    System.out.println(items.stream()
        .filter(item -> item == 2)
        .count()
    );
}
```

为了找出等于2的元素，需要对stream对象进行过滤，这里的过滤指的是：保留通过某项测试的对象。可以看见filter方法的参数是一个Lambda表达式，那么很容易想到函数接口，下面贴出这个函数接口：

```java
public interface Predicate<T> {
    boolean test(T t);
    ...
}
```

此接口还包含了很多其它方法，这些方法都被default修饰并给出了默认的实现，所以此接口还是被定义为函数接口。不管test方法在什么时候被调用，它肯定是过滤的依据。

### 实现机制

通过集合调用`stream()`方法并不会返回一个新的集合，而是创建新集合的配方。

```java
items.stream()
        .filter(item -> item == 2);
```

在运行到上面的代码时，其实并没有做出实际性的工作。filter 只刻画了Stream，但没有产生新的集合。类似这种只描述Stream，最终不产生新集合的方法叫做`惰性求值方法`；想count这样最终会从stream 产生值的方法叫做`及早求值方法`。我们加一行输出代码来证明它的惰性：

```java
items.stream()
    .filter(item -> {
        System.out.println(item);
        return item == 2
    });
```

执行上诉代码，会发现无任何输出，这就说明了filter为惰性求值方法。而在Stream 通过的API 中，分辨惰性方法的方法很简单，只需要看方法的返回值是否为Stream，若是Stream则为惰性。这种实现与设计模式中的建造者模式相似。

### 常用的流操作

#### 1）collect(toList())

此方法将Stream中的值生成一个集合，如前面的例子中过滤操作后，执行此方法，可以返回一个过滤后的集合。此方法是及早求值方法。

```java
List<Integer> result = items
	.stream()
    .filter(item -> item == 2)
    .collect(Collectors.toList());
```

#### 2）map

此方法是将stream中的每个元素都通过特定的转化方式进行转化，这是一个惰性求值方法。如将集合中的元素都减1：

```java
items.stream().map(item -> item - 1);
```

#### 3）filter

前面已经讲过，不再赘述。

#### 4）flatMap

将多个集合连接成一个stream对象，这是一个惰性求值方法。如将两个集合连接：

```java
List<Integer> items,items1 = new ArrayList<>();
items.add(1);
items.add(2);
items.add(2);
items1 = items.stream().filter(item -> item == 2).collect(Collectors.toList());

long count = Stream.of(items, items1)
    .flatMap(item -> item.stream())
    .collect(Collectors.toList())
    .count();
```

此方法与`map`方法一样，只是限定了函数接口的返回类型为Stream。

#### 5）max、min

求集合的最大值或最小值，是一个惰性求值方法。下面利用max方法求正整数集合的最小值：

```java
Integer min = items.stream()
    .max(Comparator.comparing(item -> item * -1))
    .get();
System.out.println(min);
```

#### 6）reduce

reduce 方法可以理解为是对Stream内元素的遍历，前面的count、min、max均是基于此方法的实现，由于它们比较常用，所以纳入了API中，下面就使用reduce实现count方法：

```java
Integer count = items.stream()
    .reduce(0, (count, elements) -> count = count + 1);
```

实现 min：

```java
Integer minVa = items
	.stream()
    .reduce(Integer.MAX_VALUE, (minItem, item) -> minItem > item ? item : minItem);
```

#### 7） forEach

遍历Stream内的元素，方法接收一个函数接口的实例：

```java
items.stream().forEach(item -> {
    item = item + 1;
});
```