本文总结、摘录自书籍[《Java 8 函数式编程》](https://www.amazon.cn/dp/B00VDSW7AE/ref=sr_1_1?s=books&ie=UTF8&qid=1528162560&sr=1-1&keywords=java+8%E5%87%BD%E6%95%B0%E5%BC%8F%E7%BC%96%E7%A8%8B)

## 高级集合类、收集器

### 方法引用

先写个有点鸡肋的类，主要是突出方法引用是什么个东西，下面是Person对象的操作类的定义：

```java
public class PersonOperator {

    public static String getNameAndPrefix(Supplier<Person> supplier) {
        return "person:" + supplier.get().getName();
    }

}
```

我们通过上面的静态方法来获取person对象的含固定前缀的名字，方法参数类型为一个函数接口，此函数接口如下：

```java
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
```

利用Lambda表达式创建它的一个实例：

```java
Supplier<Person> personSupplier = () -> new Person();
```

下一步结合上面的静态方法：

```java
String namePrefix = PersonOperator.getNameAndPrefix(() -> new Person());
```

接下来我们使用方法引用来替换：

```java
String namePrefix = PersonOperator.getNameAndPrefix(Person::new);
```

到此已经引出了方法引用的使用方式，上面是对Person的构造函数的引用，当然也可以引用其它方法，比如：

```java
Person::getName
```

注意：这不同于方法调用，Lambda表达式针对的永远是函数接口。不管这种方法引用的用法，只需要知道方法引用只是一种等效的简短语法。正如前面的：`() -> new Person()`与`Person::new`。

### 元素顺序

关于集合类的内容在流中以何种顺序排列。我们知道，一 些集合类型中的元素是按顺序排列的，比如 List；而另一些则是无序的，比如 HashSet。 增加了流操作后，顺序问题变得更加复杂。 直观上看，流是有序的，因为流中的元素都是按顺序处理的。这种顺序称为出现顺序。出现顺序的定义依赖于数据源和对流的操作。 在一个有序集合中创建一个流时，流中的元素就按出现顺序排列。如果集合本身就是无序的，由此生成的流也是无序的。

一些中间操作会产生顺序，比如对值做映射时，映射后的值是有序的，这种顺序就会保留 下来。如果进来的流是无序的，出去的流也是无序的。

一些操作在有序的流上开销更大，调用 unordered 方法消除这种顺序就能解决该问题。大多数操作都是在有序流上效率更高，比如 filter、map 和 reduce 等。

### 使用收集器

collect(toList())在流中生成列表。显然，List 是能想到的从流中生 成的最自然的数据结构，但是有时人们还希望从流生成其他值，比如 Map 或 Set，或者你 希望定制一个类将你想要的东西抽象出来。 前面已经讲过，仅凭流上方法的签名，就能判断出这是否是一个及早求值的操作。reduce 操作就是一个很好的例子，但有时人们希望能做得更多。 这就是收集器，一种通用的、从流生成复杂值的结构。只要将它传给 collect 方法，所有 的流就都可以使用它了。 标准类库已经提供了一些有用的收集器，比如导出一个Map:

```java
List<Integer> items = new ArrayList<>();
items.add(1);
items.add(3);
items.add(2);

Map<String, Integer> map = 
    items.stream().collect(Collectors.toMap(
          (item) -> item + "key", (item) -> item
    ));
```

#### 使用收集器转换成值

还可以利用收集器让流生成一个值。maxBy 和 minBy 允许用户按某种特定的顺序生成一个值。

```java
Integer minK = items
	.stream()
    .collect(Collectors.minBy((min1, min2)-> min1 > min2 ? 1 : -1))
    .get();
```

#### 使用收集器数据分块

另外一个常用的流操作是将其分解成两个集合。下面将集合中分为大于1与小于等于1两个集合：

```java
Map<Boolean, List<Integer>> cc = items
    .stream()
    .collect(Collectors.partitioningBy((item) -> item <= 1 ? true : false));
```

#### 使用收集器数据分组

下面对集合的数值进行分组，值相同的为一个分组：

```java
Map<Integer, List<Integer>> kk = items
	.stream()
    .collect(Collectors.groupingBy(item -> item));
```

#### 使用收集器生成字符串

下面对集合的数值转化为字符串并收集这些字符串：

```java
String gg = items
	.stream()
    .map(item -> item + "")
    .collect(Collectors.joining("-", "{","}"));
```

### Map 新方法

提出情况下，我们会从一个Map中通过键获取值：

```java
Map<String, Integer> map = ...
Integer a = map.get("a");
```

获取的值不能确定不为null，Java 8为我们引入了一个新方法`computeIfAbsent`，该方法接受键与一个Lambda表达式，若值不存在则使用Lambda表达式计算新值：

```java
Map<String, Integer> map = new HashMap<>();
map.put("a", 1);

Integer c = map.computeIfAbsent("c", (key) -> key.hashCode() * -1);
System.out.println(c);
```

compute方法：若值存在则使用Lambda表达式计算新值：

```java
Integer d = map.compute("a", (key, value) -> key.hashCode() + value);
```

forEach方法：

```java
map.forEach(
    (key, value) -> {
        System.out.println(key + ":" + value);
    }
);
```

