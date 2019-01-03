// 非注释第一行
// main 函数只能在package main包中
// 倒入的包在程序中没有被调用时，那么在编译时会报错
// go 中使用大小写来表示：变量 常量 类型 接口 结构 函数 是否能被外部调用
// 函数首字母小写为 private
// 首字母大写 public
package main

import(
	"fmt"
	"runtime"
)

// 常量的定义
const PI = 3.14
// 常量的定义
var  name = "james"
// 一般类型
type  integerType int
// 结构申明结构
type jamesStruct struct {}
// 接口申明
type jamesINterface interface {}

// https://go.fdos.me/04.2.html
func main() {
	fmt.Printf("%s", runtime.Version());
}