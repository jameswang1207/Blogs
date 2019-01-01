package main

import(
	"fmt"
    "runtime"
)
// https://go.fdos.me/04.2.html
//  go 概念解释
func main() {
	fmt.Printf("%s", runtime.Version());
}