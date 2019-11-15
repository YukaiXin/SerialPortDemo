
# SerialPortDemo
Android linux内核 /dev文件夹，全称device


在Linux系统的设备特殊文件目录/dev/下，终端特殊设备文件一般有以下几种：
## 1、串行端口终端(/dev/ttySn)
串行端口终端(Serial Port Terminal)是使用计算机串行端口连接的终端设备。计算机把每个串行端口都看作是一个字符设备。有段时间这些串行端口设备通常被称为终端设备，因为那时它的最大用途就是用来连接终端。这些串行端口所对应的设备名称是/dev/tts/0(或/dev/ttyS0), /dev/tts/1(或/dev/ttyS1)等，设备号分别是(4,0), (4,1)等，分别对应于DOS系统下的COM1、COM2等。若要向一个端口发送数据，可以在命令行上把标准输出重定向到这些特殊文件名上即可。例如，在命令行提示符下键入：echo test > /dev/ttyS1会把单词”test”发送到连接在ttyS1(COM2)端口的设备上。
## 2、伪终端(/dev/pty/)
伪终端(Pseudo Terminal)是成对的逻辑终端设备(即master和slave设备, 对master的操作会反映到slave上)。
例如/dev/ptyp3和/dev/ttyp3(或者在设备文件系统中分别是/dev/pty /m3和 /dev/pty/s3)。它们与实际物理设备并不直接相关。如果一个程序把ptyp3(master设备)看作是一个串行端口设备，则它对该端口的读/ 写操作会反映在该逻辑终端设备对应的另一个ttyp3(slave设备)上面。而ttyp3则是另一个程序用于读写操作的逻辑设备。telnet主机A就是通过“伪终端”与主机A的登录程序进行通信。
## 3、控制终端(/dev/tty)
如果当前进程有控制终端(Controlling Terminal)的话，那么/dev/tty就是当前进程的控制终端的设备特殊文件。可以使用命令”ps –ax”来查看进程与哪个控制终端相连。对于你登录的shell，/dev/tty就是你使用的终端，设备号是(5,0)。使用命令”tty”可以查看它具体对应哪个实际终端设备。/dev/tty有些类似于到实际所使用终端设备的一个联接。
## 4、控制台终端(/dev/ttyn, /dev/console)
在Linux 系统中，计算机显示器通常被称为控制台终端 (Console)。它仿真了类型为Linux的一种终端(TERM=Linux)，并且有一些设备特殊文件与之相关联：tty0、tty1、tty2 等。当你在控制台上登录时，使用的是tty1。使用Alt+[F1—F6]组合键时，我们就可以切换到tty2、tty3等上面去。tty1–tty6等称为虚拟终端，而tty0则是当前所使用虚拟终端的一个别名，系统所产生的信息会发送到该终端上（这时也叫控制台终端）。因此不管当前正在使用哪个虚拟终端，系统信息都会发送到控制台终端上。/dev/console即控制台，是与操作系统交互的设备，系统将一些信息直接输出到控制台上。目前只有在单用户模式下，才允许用户登录控制台。
## 5，虚拟终端(/dev/pts/n)
在Xwindows模式下的伪终端.如我在Kubuntu下用konsole，就是用的虚拟终端，用tty命令可看到/dev/pts/1。
## 6 其它类型
Linux系统中还针对很多不同的字符设备存在有很多其它种类的终端设备特殊文件。例如针对ISDN设备的/dev/ttyIn终端设备等。这里不再赘述。

tty设备包括虚拟控制台，串口以及伪终端设备。
/dev/tty代表当前tty设备，在当前的终端中输入 echo “hello” > /dev/tty ，都会直接显示在当前的终端中。

设备文件分为两种：块设备文件(b)和字符设备文件(c)

设备文件一般存放在/dev目录下，对常见设备文件作如下说明：

/dev/hd[a-t]：IDE设备

/dev/sd[a-z]：SCSI设备

/dev/fd[0-7]：标准软驱

/dev/md[0-31]：软raid设备

/dev/loop[0-7]：本地回环设备

/dev/ram[0-15]：内存

/dev/null：无限数据接收设备,相当于黑洞

/dev/zero：无限零资源

/dev/tty[0-63]：虚拟终端

/dev/ttyS[0-3]：串口

/dev/lp[0-3]：并口

/dev/console：控制台

/dev/fb[0-31]：framebuffer

/dev/cdrom => /dev/hdc

/dev/modem => /dev/ttyS[0-9]

/dev/pilot => /dev/ttyS[0-9]

/dev/random：随机数设备

/dev/urandom：随机数设备

(PS：随机数设备，后面我会再写篇博客总结一下)

/dev目录下的节点是怎么创建的?

devf或者udev会自动帮你创建得。

kobject是sysfs文件系统的基础，udev通过监测、检测sysfs来获取新创建的设备的。
