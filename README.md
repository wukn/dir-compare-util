### 题目：文件夹内容对比

机器上有两个目录：a b， 目录下面又有文件和文件夹，文件夹下面又有文件和文件夹，以此类推。
需要找出来A B 两个目录下面有哪些文件是不同的。
不同文件的定义：
1. 名字不一样
2. 大小不一样
3. 内容不一样
输出： 两个目录下有哪些是不同的。

### 例子

```
目录A
 |- 文件A
 |- 文件b
 |- 目录a1
    |-文件a11
    |-文件a12
 |- 目录a2
    |-文件a21
    |-文件a22
```
```
目录B
    |-文件A
    |-文件b1
    |-目录a1
        |-文件a11
        |-文件a12
        |-文件3
```

最后输出：
```
目录A/文件b
目录B/文件b1
目录A/目录a2
目录B/目录a1/文件3
```
