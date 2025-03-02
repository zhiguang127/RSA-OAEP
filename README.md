# OAEP-RSA 的实现

## 1. 介绍
南邮信息安全综合实验题目二

题目要求:

    RSA算法是应用最为广泛的公钥加密算法之一，但由于基本算法具有的同态性，在实际使用过程中，通常需要对基本的RSA算法进行改进，而RSA-OAEP算法即为实际使用算法之一。本课题致力于对RSA-OAEP算法的模拟实现，使学生深入了解基本RSA算法如何应用到实际的机密性问题解决中，提升编程能力。

## 2. 环境
- JDK:JAVA21 
- GUI:Swing
- IDE:IDEA2024.1

## 3.文件结构
```
RSA-OAEP
├── README.md                   // 说明文档
├── lib                         //依赖库
│   └── flatlaf-2.6.jar         //Swing美化库
└── src 
    ├── Main.java
    ├── com             
    │   └── rsaApp              
    │       ├── Hash            //哈希算法
    │       │   ├── MD4.java    //实现MD4算法    
    │       │   ├── MD5.java    //实现MD5算法
    │       │   └── SHA256.java //实现SHA256算法
    │       ├── OAEP_RSA
    │       │   ├── OAEP.java   //实现OAEP填充，对数据进行预处理
    │       │   ├── RSA.java    //实现RSA加密解密
    │       │   ├── RSAKeyGenertor.java //生成RSA密钥对
    │       │   └── Util.java   //工具类，主要实现字符串格式的处理
    │       ├── Window                  //Swing窗口类包
    │       │   ├── MainWindow.java     //主窗口
    │       │   └── View                //视图包
    │       │       ├── MainView.java   //主视图
    │       │       └── subView         //子视图包
    │       │           ├── HashView.java   //哈希功能视图
    │       │           ├── RsaView.java    //RSA加解密视图
    │       │           └── aboutView.java  //关于页视图
    │       └── shared                  //共享包
    │           ├── ClipboardTUtil.java //剪切板工具类
    │           └── ComponentUtil.java  //Swing组件工具类
    └── resources               //资源包，包含图片资源
        ├── Oaep-.png
        └── rsa.png
```

## 5.架构设计
- 采用了类MVC（Model-View-Controller）的架构风格，将程序分为三个部分
    - Model: 数据模型，负责处理数据
    - View: 视图，负责显示数据
    - Controller: 控制器，负责处理用户的操作

MVC模式的优点是，可以使代码更加清晰，易于维护，同时也可以使代码的重用性更高，因为模型和视图是分开的，所以可以很容易地更改视图而不影响模型，也可以很容易地更改模型而不影响视图。

在本项目中，模型（model）的工作由OAEP-RSA的算法实现来承担，视图（View）和控制器（Controller）部分由Swing的前端控件以及事件处理函数来承担，通过将应用程序划分为三个独立的组件，该课题实现了代码的低耦合度，提高可重用性和可维护性，也正因此，才可把Hash功能单独提取出来成为一个独立的功能

MVC模式如下图所示：

![1.png](https://s2.loli.net/2025/03/02/Dbn8O9fc1FzXNCp.png)

## 5.程序界面
本程序的界面采用了Swing的GUI库，整体风格采用了FlatLaf的主题，使得界面更加美观。

具体的界面如下：

RSA-OAEP主界面:
![2.png](https://s2.loli.net/2025/03/02/PmAOIS9N4CtcfnU.png)

haSH功能界面:

![3.png](https://s2.loli.net/2025/03/02/lzotSpXduTBe4H6.png)

## 6.功能介绍
### 1.RSA密钥生成及存储

### 2.RSA加密解密

### 3.OAEP填充

### 4.MD4、MD5、SHA256哈希算法



