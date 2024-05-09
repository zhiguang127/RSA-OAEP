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

## 4.功能介绍
- RSA密钥生成

    此部分


- RSA加密解密
- OAEP填充
- MD4、MD5、SHA256哈希算法
- 剪切板复制





