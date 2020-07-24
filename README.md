# check

添加jitpack仓库地址(或是你有自己的私仓，把jar包传上去也可以):
<pre>
<repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
</repository>
</pre>
引入jar包:
<pre>
<!-- support Check-->
 <dependency>
       <groupId>com.github.shi0288</groupId>
       <artifactId>check</artifactId>
       <version>1.41</version>
</dependency>
</pre>

使用样例:

<pre>
@RequestMapping("ceshi")
public Result test(
                @Check(value = "name", defaultValue = "小明") String username,
                @Check(value = "age", min = "18", max = "55") int age,
                @Check(name = "手机号", mobile = true, required = false) String mobile,
                @Check(name = "身份证", idCard = true, required = false) String card) {
        return new Result();
}
</pre>
说明:
* @Check包含@RequestParam原生注解的所有功能，唯一修改的是将name值当做返回提示中的字段名来使用。
* 当@RequestParam和@Check同时作用于方法参数上时，@Check不起作用，以@RequestParam为准。

注解校验参数使用:

参数名称	参数类型	功能	样例

value	String	映射参数名称	@Check(value = "name") String username

required	boolean	是否必填，默认为必填	true


defaultValue	String	默认值	

min	String	允许数字最小值，支持小数	1

max	String	允许数字最大值，支持小数	10

pattern	String	自定义正则表达式	^[A-Za-z]+$

name	String	提示字段名，返回message中体现	

mobile	boolean	校验是否是手机号	

email	boolean	校验是否是邮箱	

idCard	boolean	校验是否是身份证	

isDecimal	boolean	校验是否最多两位小数，可用作money	

numOrLetter	boolean	校验是否只包含数字和字母	

chinese	boolean	校验是否中文	

ip	boolean	校验是否IP格式	

url	boolean	校验是否URL格式	

date	boolean	校验是否日期 2012-12-23 23:23:23	

number	boolean	校验是否数字	

notZero	boolean	校验是否数字，且不为0	

letter	boolean	校验是否字母	

length	int	校验字符串长度	

valid	Class[]	自定义校验器	下边举例说明

自定义校验器:

继承BaseValidator 接口，实现 validate方法
<pre>public class TestValidator implements BaseValidator{
        @Override
        public void validate(Check check, String s, Object o) {
                System.out.println(check);
                System.out.println(s);
                System.out.println(o);
        }
}
</pre>
@Check(valid = TestValidator.class) String self

其中   valid为数组，可以添加N个校验器

validate方法中参数：

check为当前参数注解对象可以获取所需信息。

s为当前参数名称。

o为当前参数值。

校验失败请自行抛出异常处理。

校验失败时，可以拦截ValidateException类型异常，可以通过异常获取BindResult对象，并从对象中拿到校验失败时的参数名，参数值及message。




