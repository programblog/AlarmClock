# AlarmClock
想要实现一个比普通闹钟更容易把人叫醒的闹钟。  
和普通的闹钟相比，主要想实现:  
1. 摇手机N次关闹钟、拍同一场景关闭闹钟、算算术题关闭闹钟  
2. 闹钟响起之后不能关机、不能减小音量，只有满足自己之前设定的条件才能停止


## 更新日志
### 2016.03.31  
修改为默认24小时制  
添加闹钟界面的闹钟标签  

### 2016.04.01  
闹钟时间格式化  
点击进入某个闹钟，TimePicker的时间和原来设定的值相同  
修复闹钟删除BUG  
响铃恢复正常  
  
### 2016.04.02  
Android的兼容性调整

### 2016.04.03  
添加“重复”响铃设置  
增加“小睡时间”设定

### 2016.04.04
响铃界面优化  

### 2016.04.05
修复闹钟关了还会响的BUG

### 2016.04.08
调整数据存储方式，修改为以JSON文件形式存储

### 2016.04.10
补全部分功能和显示Bug
修复闹钟覆盖问题
添加关闭闹钟选项，具体功能待实现
增加开机自启动，防止手机重启后闹钟不响