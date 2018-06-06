本文总结、摘录自书籍[《Java 8 函数式编程》](https://www.amazon.cn/dp/B00VDSW7AE/ref=sr_1_1?s=books&ie=UTF8&qid=1528162560&sr=1-1&keywords=java+8%E5%87%BD%E6%95%B0%E5%BC%8F%E7%BC%96%E7%A8%8B)

## Lambda

### 函数式编程

为了编写这类处理批量数据的并行类库，需要在语言层面上修改现有的 Java：增加 Lambda 表达式。面向对象编程是对数据进行抽象，而函数式编程是对行为进行抽象。在写回调函数和事件处理程序时，程序员不必再纠缠于匿名内部类的冗繁和可读性，函数式编程让事件处理系统变得更加简单。能将函数方便地传递也让编写惰性代码变得容易，惰性代码在真正需要时才初始化变量的值。

#### 什么是函数式编程

在思考问题时，使用不可变值和函数，函数对一个值进行处理，映射成另一个值。

### Lambda

#### 例子

```java
JButton button = new JButton("click me");

button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("clicked");
    }
});
```

等同于：

```java
button.addActionListener(event -> System.out.println("clicked"));
```

#### 不同的形式

1） 无参数

```java
/**
 * 可打印的抽象接口
 */
public interface Printable {
    void print();
}

public void testPrintable() {
    Printable printable = () -> System.out.println("aaa");
    printable.print();
}
```

2） 一个参数

```java
public interface Printable {
    void print(String arg);
}

Printable printable = arg -> System.out.println(arg);
```

3）多个语句

```java
Printable printable = arg -> {
    System.out.println("one line");
    System.out.println(arg);
};
```

4）多个参数

```java
public interface PrintableMultiParams {
    void print(String print, boolean isPrint);
}

PrintableMultiParams printableMultiParams = (arg, isPrint) -> {
    if(isPrint) {
        System.out.println(arg);
    }
};
```

Lambda 表达式的参数类型可以由编译器推断出来，但是最好显式声明参数类型：

```java
PrintableMultiParams printableMultiParams = (String arg, boolean isPrint) -> {
    if(isPrint) {
        System.out.println(arg);
    }
};
```

### Lambda 是静态类型

下面引入一种情况：

```java
String name;

PrintableMultiParams printableMultiParams = (arg, isPrint) -> {
    if(isPrint) {
        System.out.println(arg + name);
    }
};
```

上面的例子，在编译阶段就会报错。为什么呢？我们来看IDEA的编译器给我们提示了什么：

```html
Variable 'name' might not have been initialized
```

意思是`name`没有初始化。我们在声明什么样子的变量时，会有这样的提示呢，或者说会有初始化的约束呢？答案就是`final`变量了。声明为final意味着不能为其重复赋值。实际上final变量可以被认为一个不可变的值。当然仅仅是不能改变引用指向的对象，并不是对象本身不可变，也就是说下面是可行的，这一点要特别注意，针对这一点，我很反感大量的博客说final变量是不可变的，却没有说明对象是可变的：

```java
final Person person = new Person("name");

person.setName("Lisa");
```

你可能会说，在这种情况下编译通过并且正常执行：

```java
String name = "vvv";

PrintableMultiParams printableMultiParams = (arg, isPrint) -> {
    if(isPrint) {
        System.out.println(arg + name);
    }
};
```

那么我们尝试着对其做些改变：

```java
String name = "vvv";

name = "ccc";

PrintableMultiParams printableMultiParams = (arg, isPrint) -> {
    if(isPrint) {
        System.out.println(arg + name);
    }
};
```

下面是IDEA编译器给我们的提示：

```xml
Variable used in lambda expression should be final or effectively final
```

这种行为也解释了为什么 Lambda 表达式也被称为闭包。未赋值的变量与周边环境隔离起来，进而被绑定到一个特定的值。在众说纷纭的计算机编程语言圈子里，Java 是否拥有真正的闭包一直备受争议，因为在 Java 中只能引用既成事实上的 final 变量。名字虽异，功能相同，就好比把菠萝叫作凤梨，其实都是同一种水果。

### 函数接口

Lambda 表达式本身的类型：函数接口。函数接口指只有一个抽象方法的接口。前面的例子中的接口均符合函数接口的定义，如：

```java
public interface PrintableMultiParams {
    void print(String print, boolean isPrint);
}
```

可以得出以下约束：

- 接口
- 一个抽象方法

注意：并没有对函数的返回值做任何限制，`void`或其它类型或泛型都是OK的。

### 类型推断

Lambda 表达式的参数类型可以由编译器推断出来。有时省略类型信息可以减少干扰，更易弄清状况；而有时却需要类型信息帮助理解代码。Lambda 表达式中的类型推断，实际上是 Java 7 就引入的目标类型推断的扩展，如下面的符号`<>`：

```java
List<String> list = new ArrayList<>();
```

Java 7 中程序员可省略构造函数的泛型类型，Java 8 更进一步，程序员可省略 Lambda 表达式中的所有参数类型。编译器可根据表达式的上下文推断出参数的类型，这就是所谓的类型推断。

### 例子

Java 8 中对ThreadLocal新增了一个创建此类实例的方法：

```java
public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
    return new SuppliedThreadLocal<>(supplier);
}
```

```java
public interface Supplier<T> {
    T get();
}
```

不管`SuppliedThreadLocal`这个内部类是怎么实现的，我们只需要知道通过上面的静态方法可以创建一个实例，而静态方法需要一个函数接口`Supplier`类型的实例，既然是函数接口，Lambda就可以派上用场了：

```java
public void testThreadLocal() {
    ThreadLocal<Date> local = ThreadLocal.withInitial(() -> {
        return new Date();
    });

    System.out.println(local.get());
}
```