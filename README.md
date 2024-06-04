<h2 align="center"> 系列 I · 第三弹 | 暮空系列 </h2>
<h3 align="center">(WIP) MK-JoinMessage | MC 服务器进服消息自定义插件</h3>

<div align="center">
    <img src="https://img.shields.io/badge/Kotlin-1.9.23-gray?style=flat&labelColor=purple" alt=""/>
    <img src="https://img.shields.io/badge/Minecraft-1.9_~_1.20.6-gray?style=flat&labelColor=green" alt=""/>
    <img src="https://img.shields.io/badge/Release-SakuraOcean_V1-gray?style=flat&labelColor=pink" alt=""/>
    <img src="https://img.shields.io/badge/DEV-1-gray?style=flat&labelColor=red" alt=""/>
</div>

---

<p align="center" style="font-weight: bold">MADE IN SAKURA OCEAN | “纵使深情，难换樱花。”</p>

<p align="center" style="font-weight: bold">Designed by Mu_Cloud</p>

___
### 功能 & 特性
- 自定义玩家进入服务器时的提示语
- 三种显示方法：聊天栏 | Boss栏 | 工具栏
- 成组管理进服提示
- 使不同的用户组下的玩家差异化显示进入服务器时的提示语
- 实现特定的用户组隐身化进服（进服不对外提示）
- 不需要更改设置文件，全部操作通过指令进行，数据与设置通过 SQLite 存储

### 指令  
_**本插件所有指令仅管理员可用 | [] 标注的为必填参数 | () 标注的为可选参数**_  
_**消息支持 '&' 颜色字符**_

**/mkjm info**  
显示插件的版本和指令信息  

**/mkjm addgroup [组名] (进服消息) (退服消息)**  
新建一个组  

**/mkjm delgroup [组名]**  
删除一个组  

**/mkjm vanish [玩家]**  
将指定的玩家移入隐身组  

**/mkjm add [组名] [玩家名]**  
将指定玩家移入指定组  

**/mkjm del [组名] [玩家名]**  
将指定的玩家移出指定的组  

**/mkjm set [组名] [进服消息] [退服消息]**  
统一设置指定组的自定义消息  

**/mkjm setjm [组名] [进服消息]**  
设置指定组的进服消息  

**/mkjm setqm [组名] [退服消息]**  
设置指定组的退服消息  

**/mkjm setmode [组名] [模式]**  
设置指定组的消息显示模式, 模式仅允许: msg | boss | action  

**/mkjm list**  
显示当前已建立的组  

**/mkjm find [玩家]**  
查找玩家在哪个组  

**/mkjm infgroup [组名]**  
打印指定组的详细信息   
