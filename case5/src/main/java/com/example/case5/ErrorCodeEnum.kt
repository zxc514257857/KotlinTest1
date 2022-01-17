package com.example.case5

import java.lang.IllegalArgumentException
import kotlin.jvm.Throws


/**
*  @Des:  全局错误码
*        统一定义设备中错误的类型
*
*
*  @Project: com.whpe.pos.common
*  @Title:
*  @Package: com.whpe.pos.common
*  @Author: tomyang
*  @Email: sheep3652011@foxmail.com
*  @Date:   21-1-21  上午11:49
*  @Version: V1.0
**/
enum class ErrorCodeEnum(val code: Int, val description: String) {

    /** 外设*/
    DEVICE_NOT_EXIST(1000, "设备不存在"),
    DEVICE_BEEP_FAILED(1001, "蜂鸣器操作失败"),
    DEVICE_UART_FAILED(1002, "串口通讯失败"),
    DEVICE_GREEN_FAILED(1003, "绿灯控制失败"),
    DEVICE_RED_FAILED(1004, "红灯控制失败"),
    DEVICE_WHITE_FAILED(1005, "白灯控制失败"),
    DEVICE_DISTANCE_FAILED(1006, "距离传感器通讯失败"),
    DEVICE_PROXIMITY_FAILED(1007, "接近传感器接入失败"),
    DEVICE_SIDE_TOUCH_KEY_FAILED(1008, "侧边触控按键通讯失败"),
    DEVICE_SIDE_FRONT_KEY_FAILED(1009, "前置触控按键通讯失败"),
    DEVICE_POWER_PIN_MONITOR_FAILED(1010, "电源监控脚监控失败"),
    DEVICE_QRCODE_CAM_READ_FAILED(1011, "二维码扫码头读取错误"),
    DEVICE_QRCODE_CAM_CLOSE_FAILED(1012, "二维码扫码头关闭失败"),
    DEVICE_NANDAGPS_OPEN_FAILED(1013, "南大gps调度数据读取错误"),
    DEVICE_NANDAGPS_READ_FAILED(1014, "南大gps调度数据读取错误"),
    DEVICE_NANDAGPS_CLOSE_FAILED(1015, "南大gps调度关闭失败"),
    DEVICE_NANDAGPS_STATIONINDEX_ILLEGAL(1016, "南大gps调度发送站点编号无效"),
    DEVICE_FRONT_CARD_FINDCARD_FAILED(1017, "前置天线寻卡失败"),
    DEVICE_TEMPERATURE_READ_LAST_TEMP_FAILED(1018, "获取上次测温数据失败"),
    DEVICE_TEMPERATURE_CLOSE_FAILED(1019, "测温模块关闭失败"),
    DEVICE_TEMPERATURE_INIT_FAILED(1020, "测温模块初始化失败"),
    DEVICE_SENSOR_LIGHT_INIT_FAILED(1021, "光线传感器初始化失败"),



    /** 配置文件相关*/
    CONFIG_FILE_INEXISTENCE(1100, "配置文件不存在"),
    CONFIG_FILE_BANK(1101, "银联配置文件异常"),

    /** 网络： 和前置相关*/
    GPRS_DISCONNECT(1200, "设备到前置网络中断"),
    GPRS_UNKNOW(1201, "不明原因的网络故障"),
    GPRS_SIGN_FAILED(1202, "设备向前置签到失败"),
    GPRS_HEARTBEAT_FAILED(1203, "设备到前置心跳失败"),
    GPRS_COREPARAM_FAILED(1204, "核心参数上送失败"),
    GPRS_REALTIME_FACEFEATURE_FEEDBACK_FAILED(1205, "实时人脸特征值反馈失败"),
    GPRS_TASKREPORT_FAILED(1206, "中断任务上送失败"),
    GPRS_FACE_TRADE_UPLOAD_FAILED(1207, "人脸交易记录上送失败"),
    GPRS_FACE_FEATURE_REQUEST_FAILED(1208, "人脸特征值请求失败"),
    GPRS_UNION_TRADE_ODA_UPLOAD_FAILED(1209, "银联交易记录上送失败"),
    GPRS_ICCARD_BLACKLIST_REQUEST_FAILED(1210, "公交卡黑名单请求失败"),
    GPRS_QRCODE_KEY_REQUEST_FAILED(1211, "二维码密钥请求失败"),
    GPRS_QRCODE_TRADE_UPLOAD_FAILED(1212, "二维码交易记录上送失败"),
    GPRS_ICCARD_TRADE_UPLOAD_FAILED(1213, "公交卡交易记录上送失败"),
    GPRS_ONLINE_QRCODE_CHECK_FAILED(1214, "在线二维码校验失败"),
    GPRS_ONLINE_HEALTHCODE_CHECK_FAILED(1215, "在线健康码校验失败"),
    GPRS_RESOURCE_REQUEST_FAILED(1216, "背景资源获取失败"),
    GPRS_UNIONKEY_REQUEST_FAILED(1217, "银联密钥获取失败"),
    GPRS_ONLINE_BLACKLIST_CHECK_FAILED(1218, "在线黑名单校验失败"),
    GPRS_ONLINE_FACE_CHECK_FAILED(1219, "在线人脸识别失败"),

    GPRS_SYSTEMTIME_CHANGE_FAILED(1220, "系统较时失败"),
    GPRS_HEARTBEAT_REQUEST_FAILED(1221, "心跳执行失败"),
    GPRS_HEARTBEAT_TASKTYPE_UNSUPPORTED(1222, "心跳中断任务类型暂不支持"),
    GPRS_HEARTBEAT_TASK_FILE_FAILED(1223, "心跳中断任务文件下载执行异常"),
    GPRS_HEARTBEAT_TASK_FILE_DUPLICATE(1224, "心跳中断任务文件下载存在重复任务"),
    GPRS_HEARTBEAT_TASK_FILE_DOWNLOAD_ERROR(1225, "心跳中断任务下载错误"),
    GPRS_HEARTBEAT_TASK_REQUEST_FAILED(1226, "心跳中断任务请求失败"),
    GPRS_HEARTBEAT_TASK_PARAM_BASEINFO_FAILED(1227, "心跳中断任务基础参数设置失败"),
    GPRS_HEARTBEAT_TASK_PARAM_BUSSET_FAILED(1228, "心跳中断任务车辆参数设置失败"),
    GPRS_HEARTBEAT_TASK_PARAM_COMMNICATEINFO_FAILED(1229, "心跳中断任务修改通讯参数失败"),
    GPRS_HEARTBEAT_TASK_PARAM_REBOOT_FAILED(1230, "心跳中断任务重启失败"),
    GPRS_HEARTBEAT_TASKREPORT_FAILED(1231, "心跳中断任务反馈失败"),
    GPRS_HEARTBEAT_TASKREPORT_DELETE_FAILED(1232, "心跳中断任务反馈记录删除失败"),
    GPRS_HEARTBEAT_TASKREPORT_PROCESS_FAILED(1233, "心跳中断任务反馈应答处理失败"),
    GPRS_COREPARAM_CONCAT_FRAME_FAILED(1234, "核心参数上送报文拼接失败"),
    GPRS_ICCARD_BLACKLIST_REQUEST_MERCHANTID_CONFLICT(1235, "卡黑名单下载商户信息和设备商户信息不一致"),
    GPRS_QRCODEKEY_DOWNLOAD_SAVEKEY_FAILED(1236, "二维码密钥下载保存失败"),
    GPRS_UNIONKEY_SAMEASLOCAL(1237, "银联联机下发密钥和本地一致，不进行保存"),
    GPRS_UNIONKEY_FORMAT_ERROR(1238, "银联联机下发密钥格式异常（空或其它）"),
    GPRS_UNIONKEY_UPDATE_FAILED(1239, "银联联机下发密钥更新失败"),
    GPRS_FACE_TRADE_UPLOAD_REFUSED(1240, "人脸交易记录上送被拒"),
    GPRS_FACE_TRAPDE_UPLOAD_TRANS_TABLE_FAILED(1241, "人脸交易记录上送成功后转表失败"),
    GPRS_ICCARD_TRADE_UPLOAD_RECORD_TRANS_FAILED(1242, "卡片交易记录转换上送记录失败"),
    GPRS_ICCARD_TRADE_UPLOAD_RECORD_QUERY_FAILED(1243, "卡片交易记录获取失败"),
    GPRS_ICCARD_TRADE_UPLOAD_REFUSED(1244, "卡片交易记录上送被拒"),
    GPRS_ICCARD_TRADE_UPLOAD_TRANS_TABLE_FAILED(1245, "卡片交易记录上送成功后转表失败"),
    GPRS_UNION_TRADE_ODA_UPLOAD_REFUSED(1246, "银联ODA交易上送被拒"),
    GPRS_UNION_TRADE_ODA_UPLOAD_TRANS_TABLE_FAILED(1247, "银联ODA交易数据上送成功后转表失败"),
    GPRS_QRCODE_TRADE_UPLOAD_REFUSED(1248, "二维码交易记录上送被拒"),
    GPRS_HEARTBEAT_INTERRUPT_LOG_UPLOAD_FAILED(1249, "心跳中断任务日志上送处理失败"),
    GPRS_HEARTBEAT_INTERRUPT_LOG_UPLOAD_TYPE_UNSUPPORTED(1250, "心跳中断任务日志上送类型不支持"),
    GPRS_HEARTBEAT_INTERRUPT_DB_UPLOAD_FAILED(1251, "心跳中断任务数据库上送类型不支持"),
    GPRS_HEARTBEAT_INTERRUPT_SPECIFIEDFILE_UPLOAD_FAILED(1252, "心跳中断任务指定文件上送类型不支持"),
    GPRS_HEARTBEAT_INTERRUPT_LOG_FILE_UPLOAD_FAILED(1253, "心跳中断任务日志上送文件（日志、数据库、指定文件）失败"),
    GRPS_HEARTBEAT_INTERRUPT_LOG_FILE_UPLOAD_REFUSED(1254, "心跳中断任务日志上送文件（日志、数据库、指定文件）被拒"),
    GPRS_HEARTBEAT_INTERRUPT_TASKREPORT_ADD_FAILED(1255, "心跳中断任务反馈任务添加失败"),
    GPRS_HEARTBEAT_INTERRUPT_DOWNLOAD_FILE_TYPE_UNSUPPORTED(1256,"心跳中断任务文件下载类型不支持"),
    GPRS_TRADE_RECORD_MONITOR_SAVE_RECORD_UPDATE_FAILED(1257, "监听交易记录入库结果变更失败（触发立即上送记录）"),
    GPRS_HEARTBEAT_LIKE_FIXJOB_DOING_FAILED(1258, "心跳类定时任务执行失败"),
    GPRS_TRADE_FIXJOB_DOING_FAILED(1259, "交易记录上送定时任务执行失败"),
    GPRS_FACE_FEATURE_REQUEST_DOING_FAILED(1260, "人脸特征值请求执行失败"),
    GPRS_TRADE_UPLIMIT_MONITOR_FAILED(1261, "交易记录上限监听失败"),
    GPRS_REALTIME_FACE_WHITELIST_REQEUST_FAILED(1262, "实时人脸白名单请求失败"),
    GPRS_FACE_BLACKLIST_FILE_REQUEST_FAILED(1263, "人脸黑名单文件查询请求失败"),
    GPRS_IDCARD_TRADE_UPLOAD_FAILED(1264, "身份证交易记录上送失败"),
    GPRS_IDCARD_BLACKLIST_REQUEST_FAILED(1265, "身份证黑白名单查询失败"),
    GPRS_IDCARD_BLACKLIST_VERSION_FEEDBACK_FAILED(1266, "身份证黑白名单版本反馈失败"),
    GPRS_FACE_FILE_FEED_BACK_FAILED(1267, "人脸特征值离线文件下载反馈失败"),
    
    GPRS_POS_INFO_PUSH_FAILED(1266, "POS信息上送失败"),

    SYSTEM_TIME_ASYNCHRONOUS(1300, "系统时间和后台时间相差大于五分钟"),
    SYSTEM_LOW_MEMORY(1301, "系统内存低,开始准备回收"),
    SYSTEM_TRIM_MEMORY(1302, "系统内存开始降低"),
    SYSTEM_STORAGE_LOW(1303, "系统存储空间低"),
    SYSTEM_FILE_DOWNLOAD_INIT_FAILED(1304, "文件初始化失败，context为空"),
    SYSTEM_FILE_DOWNLOAD_FAILED(1305, "文件下载失败"),
    SYSTEM_FILE_UNZIP_FAILED(1306, "文件解压失败"),
    SYSTEM_FILE_DOWNLOAD_FILE_MD5_CHECK_FAILED(1307, "文件下载md5校验失败"),
    SYSTEM_FILE_CITY_WHITELIST_FILE_PARSE_FAILED(1308, "城市白名单解析失败"),
    SYSTEM_CONFIGFILE_UNSUPPORTED_PATH(1309, "配置文件下载路径不支持"),
    SYSTEM_CONFIGFILE_UNSUPPORTED_FILE(1310,"配置文件下载文件不支持"),
    SYSTEM_CONFIGFILE_PARSE_FAILED(1311, "配置文件解析失败"),
    SYSTEM_UDISK_PATH_QUERY_FAILED(1312, "U盘挂载路径获取失败"),
    SYSTEM_UDISK_FILE_NOT_EXIST(1313, "U盘挂载路径下文件不存在"),
    SYSTEM_SOUND_PLAY_ERROR(1314, "音频播放错误"),
    SYSTEM_SOUND_SOURCE_RELEASE_FAILED(1315, "音频播放资源释放失败"),
    SYSTEM_SOUND_FILE_NOT_EXIST(1316, "音频文件不存在"),
    SYSTEM_FILE_SECTIONAL_FARE_PARSE_FAILED(1317, "分段票价文件解析失败"),
    SYSTEM_FILE_SECTIONAL_FARE_LINENO_UNMATCH(1318, "分段票价文件线路号和设备线路号不匹配"),
    SYSTEM_FILE_TRADERULE_COPY2TEMP_FAILED(1319, "交易规则文件拷贝至缓存文件夹失败"),
    SYSTEM_FILE_BLACKLIST_PARSE_FAILED(1320, "黑名单解析失败"),
    SYSTEM_APK_START_INSTALL_FAILED(1321, "启动应用更新失败"),
    SYSTEM_TICKETPRICE_ILLEGAL_TRADE_FORBIDDEN(1322, "当前票价非法，禁止交易"),
    SYSTEM_UDISK_MOUNT_FREQUENTLY(1323, "U盘挂载频繁"),
    SYSTEM_FILE_APK_INSTALL_VERIFY_FAILED(1324, "程序更新文件校验失败"),
    SYSTEM_TICKETPRICE_MONITOR_PRICE_CHANGE_FAILED(1325, "监听票价变更失败"),
    SYSTEM_TICKETPRICE_DISCOUNT_PRICE_QUERY_FAILED(1326, "折扣率票价查询转换失败"),
    SYSTEM_FACE_ACTION_MONITOR_FAILED(1327, "人脸算法操作监听失败"),
    SYSTEM_TRIGGER_POWEROFF_FAILED(1328, "触发自动关机失败"),
    SYSTEM_TRIGGER_REBOOT_FAILED(1329, "触发自动重启失败"),
    SYSTEM_FACE_BLACKLIST_PARSE_FAILED(1330, "人脸黑名单文件解析失败"),
    SYSTEM_TASK_REMOVED(1332, "系统服务被关闭"),

    
    SYSTEM_CPU_OCCUPY_HIGH(1331, "CPU占用高于90%"),

    /** 数据库相关错误  */
    DB_LACK_OF_BASEINFO(1401, "缺少设备信息"),
    DB_DATA_FULL(1402, "数据已满"),
    DB_FACE_QUERY_VERSION_FAILED(1403, "人脸特征值版本查询失败"),
    DB_CITY_WHITELIST_ACTION_FAILED(1404, "城市白名单库操作失败"),
    DB_SECTIONAL_FARE_INSERT_FAILED(1405, "分段票价信息入库失败"),
    DB_SECTIONAL_FARE_QUERY_FAILED(1406, "分段票价查询失败"),
    DB_BLACKLIST_INSERT_FAILED(1407, "黑名单表新增入库失败"),
    DB_BLACKLIST_DELETE_FAILED(1408, "黑名单表删除失败"),
    DB_UNIONTRADEBASEINFO_INSERT_FAILED(1409, "银联/银商交易参数插入失败"),
    DB_UNIONTRADEBASEINFO_DELETE_FAILED(1410, "银联/银商交易参数删除失败"),
    DB_UNIONTRADEBASEINFO_UPDATE_FAILED(1411, "银联/银商交易参数更新失败"),
    DB_UNIONTRADEBASEINFO_QUERY_FAILED(1412, "银联/银商交易参数查询失败"),
    DB_FACE_BLACKLIST_INSERT_FAILED(1413, "人脸黑名单插入失败"),

    /** 卡片相关 */
    CARD_UNENABLE(2101, "卡未启用"),
    CARD_UNREACH_ENABLE_DATE(2102, "卡片未到启用日期"),
    CARD_OVERDUE(2103,"卡片已过有效期"),
    CARD_BLACKLIST(2104, "黑名单卡"),
    CARD_LOCKED(2105, "已锁卡"),
    CARD_ENABLE_CITYUNION(2106,"互联互通未启用"),
    CARD_INVALID_ALLOPATRY(2107, "无效异地卡"),
    CARD_NEED_ANNUAL(2108, "请年审"),
    CARD_LEAD_PROHIBIT(2109,"禁止带人"),
    CARD_USELESS_AT_THIS_CAR(2110, "此卡不能在本车使用"),
    CARD_BALANCE_ILLEGAL(2111,"余额超限"),
    CARD_BALANCE_INSUFFICIENT(2112, "余额不足，请投币"),
    CARD_BALANCE_LOW(2113,"余额不多，请及时充值"),
    CARD_VERIFY_PIN_FAILED(2114, "Pin卡失败"),
    CARD_CASH(2115, "请投币"),
    CARD_INVALID(2116,"无效卡"),
    CARD_PSAM_ERROR(2117, "PSAM卡错误"),
    CARD_DEDUCT_ERROR(2118, "扣费失败"),
    CARD_TRADE_AMOUNT_ILLEGAL(2119,"票价错误"),
    CARD_CONSUME_AGAIN(2120, "请重刷"),
    CARD_SAVE_RECORD_FAIL(2121, "交易记录保存失败"),
    CARD_CREDIT_MAC_FAIL(2122, "验证mac2失败"),
    /** 司机卡  */
    CARD_DRIVER_NEED_SWIPE(2124, "请刷司机卡并检查时间"),
    /** 调价卡*/
    CARD_MANAGER_ADJUST_CARD_PARSE_FAILED(2125, "调价卡解析失败"),
    /** 卡片结构错误 */
    CARD_STRUCTURE_ERROR(2126, "卡结构错误"),
    /** 卡片交易记录达到上限 */
    CARD_RECORD_UP_TO_LIMIT(2127, "卡片交易记录超限"),


    /** 银行卡相关 */
    BANK_TEMPORARILY_UNAVAILABLE(2201, "暂时不能使用"),
    BANK_BIN_INVALID(2202,"卡bin不在白名单"),
    BANK_CONSUME_AGAIN(2203, "请重刷"),
    BANK_NET_UNREACHABLE(2204, "网络链接失败"),
    BANK_RECEIVE_OUTOFTIME(2205,"网络接收超时"),
    BANK_GPO_FAIL(2206, "GPO操作失败"),
    BANK_READ_AFL_FAIL(2207, "读AFL失败"),
    BANK_RESPONSE_ERROR(2208,"银联响应内容错误"),
    BANK_LOST_CARD(2209, "挂失卡"),
    BANK_BALANCE_NOT_ENOUGH(2210, "余额不足"),
    BANK_OPEN_UNION_FREE(2211,"请开通双免"),
    BANK_SIGN_FAIL(2212, "签到失败"),
    BANK_SET_PARAM(2213, "设置银联参数成功"),
    BANK_PLEASE_TAKE_CARD(2214,"提示取走卡片或拿开手机"),
    BANK_AUTHENTICATION_FAIL(2215, "公网认证失败"),
    BANK_TMS_KEY1_NOT_EXIST(2216, "分量一不存在"),
    BANK_TMS_KEY2_NOT_EXIST(2217, "分量二不存在"),
    BANK_UNION_HEADER_NOT_EXIST(2218, "银联交易头不存在"),
    BANK_UNION_TPDU_NOT_EXIST(2219, "银联TPDU不存在"),
    BANK_UNION_IP_NOT_EXIST(2220, "银联IP不存在"),
    BANK_UNION_PORT_NOT_EXIST(2221, "银联IP不存在"),
    BANK_UNION_MCHNTID_NOT_EXIST(2222, "银联商户号不存在"),
    BANK_UNION_TERID_NOT_EXIST(2223, "银联终端号不存在"),
    BANK_UNION_KEK_NOT_EXIST(2224, "银联交易密钥不存在"),
    BANK_UNION_PARAM_NOT_EXIST(2225, "银联交易参数缺失"),

    /** 二维码相关 */
    QRCODE_TEMPORARILY_UNAVAILABLE(2301, "暂不能使用"),
    QRCODE_FORMAT_ILLEGAL(2302,"二维码格式错误"),
    QRCODE_OVERDUE(2303, "二维码过期"),
    QRCODE_KEY_OVERDUE(2304,"二维码密钥过期，请联网后刷新二维码"),
    QRCODE_PARAMETER_ILLEGAL(2305, "传入参数错误"),
    QRCODE_TICKET_OVER_TOP_LIMIT(2306,"单笔限额超限"),
    QRCODE_INIT_FIRST(2307, "未初始化，请先初始化"),
    QRCODE_UNSUPPORT_CARDTYPE(2308,"卡类型不支持"),
    QRCODE_SYSTEM_EXCEPTION(2309, "系统异常"),
    QRCODE_REUSED(2310, "二维码重复使用"),
    QRCODE_LESS_THAN_5_SECONDS(2311,"同一用户5秒内不能多次刷码"),
    QRCODE_KEY_ERROR(2312, "二维码密钥有误"),
    QRCODE_CARD_CERTIFICATE_ERROR(2313,"卡证书有误"),
    QRCODE_PARSE_ERROR(2314, "二维码解析出错"),
    QRCODE_TIME_NOT_SYNC(2315,"时间不同步"),
    QRCODE_PROTO_UNSUPPORTED(2316, "不支持的协议版本"),
    QRCODE_INSTITUTION_NOT_SUPPORT(2317,"机构不支持"),
    QRCODE_INIT_DUPLICATED(2318, "重复初始化"),
    QRCODE_POWER_OFF(2319, "即将断电"),
    QRCODE_FAILED(2320,"支付失败"),
    QRCODE_QUERY_FAILED(2321, "查询失败"),
    QRCODE_QUERYING(2322, "正在查询"),
//    QRCODE_HEALTH_GREEN(2323,"绿码请通行"),
    QRCODE_HEALTH_RED(2324, "健康异常"),
    QRCODE_INVALID(2325, "无效码"),
    QRCODE_VERIFY_FAILED(2326, "验证失败"),
    QRCODE_PRICE_ILLEGAL(2327, "票价错误"),
    QRCODE_BALANCE_NOT_ENOUGH(2328, "余额不足"),

    /** 人脸识别相关 */
    FACE_UNMATCH(2401, "人脸不匹配"),
    FACE_REGIST_FIRST(2402, "未识别人脸信息，请先注册"),
    FACE_AIM_CAMERA(2403,"请正对屏幕"),
    FACE_AIM_EDGE_PROFILE(2404, "请将人脸置于轮廓内"),
    FACE_VERIFY_OUTOFTIME(2405,"验证超时"),
    FACE_CHIP_INIT_FAIL(2406, "加密芯片初始化失败"),
    FACE_LOAD_FEATURE_FAIL(2407,"未加载特征值文件"),
    FACE_LOAD_TRACK_MODEL_FAIL(2408, "加载人脸追踪模型失败"),
    FACE_LOAD_ANALYZER_MODEL_FAIL(2409,"加载人脸检测模型失败"),
    FACE_LOAD_VERIFY_MODEL_FAIL(2410, "加载人脸比对模型失败"),
    FACE_DO_NOT_BLOCK_FACE(2411,"请勿遮挡面部"),
    FACE_REPEAT_TRADE(2412, "重复交易"),
    FACE_IS_RECOGNIZING(2413, "识别中"),
    FACE_NOT_PREPARED(2414,"人脸识别正在初始化中，请稍后重试"),
    FACE_VERIFY_FAILED(2415, "验证失败"),
    FACE_BLACKLIST(2416, "人脸黑名单"),
    FACE_CONSUME_STATUS_LOCKED(2417,"消费状态锁定"),
    FACE_ILLEGALLIST(2418, "危险人员"),
    FACE_RECORD_OVERLIMIT(2419, "交易记录超限"),
    FACE_OUT_OF_DATE(2420,"超过有效期"),
    FACE_SAVE_RECORD_ERROR(2421, "保存数据异常"),
    FACE_SAVE_RECORD_FAIL(2422,"交易记录保存失败"),
    FACE_SAVE_FACE_PIC_FAIL(2423, "保存人脸照片失败"),
    FACE_CANNOT_TRADE_NOW(2424, "暂时不能交易"),
    FACE_NOT_DRIVER(2425,"非司机，不能签到"),
    FACE_NO_FEATURES(2426, "无特征值，请稍后重试"),
    FACE_DB_EXCEPTION(2427, "验证失败 427"),
    FACE_DB_SOURCE_ERROR(2428,"特征值内部加载错误"),
    FACE_TEMPORARILY_UNAVAILABLE(2429, "人脸识别暂时无法使用"),
    FACE_NO_MODELS(2430, "缺少模型文件"),
    FACE_INITIALING(2431,"人脸识别正在初始化"),
    /** 人脸账户不存在  */
    FACE_ACCOUNT_NOT_EXIST(2432, "账户不存在"),
    /** 人脸未注册 */
    FACE_NOT_REGISTERED(2433, "用户未注册"),


    /** 阿里人脸识别*/
    FACE_ALI_INTERNAL_ERROR(2433, "阿里算法内部错误"),
    FACE_ALI_CANCEL_BY_USER(2434, "阿里算法用户取消"),
    FACE_ALI_TIMEOUT_BY_USER(2435, "阿里算法超时"),
    FACE_ALI_VERIFY_FAILED(2436, "阿里算法验证失败"),
    FACE_ALI_DETECT_EXIT(2437, "阿里算法探测退出"),
    FACE_ALI_UNKNOWN(2438, "阿里算法不明原因错误"),
    FACE_ALI_INIT_FAILED(2439, "阿里算法初始化失败"),
    FACE_ALI_REGIST_FAILED(2440, "阿里算法注册失败"),
    FACE_ALI_START_FACE_DETECT_FAILED(2441, "阿里算法启动人脸识别失败"),
    FACE_ALI_CAMERA_DISCONNECT(2442, "阿里算法摄像头断开"),
    FACE_ALI_DB_QUERY_FAILED(2443, "阿里算法数据库查询失败"),
    FACE_ALI_VERIFY_RESULT_ILLEGAL(2444, "阿里算法返回参数数据缺失"),
    FACE_ALI_FUNCTION_FORBIDDEN_BY_SERVER(2445, "人脸功能禁用，请联系厂家"),
    FACE_ALI_RESULT_CALLBACK_PROCESS_FAILED(2446, "人脸识别完成后，处理回调失败"),
    FACE_ALI_RECORD_UPLIMIT_MONITOR_FAILED(2447, "人脸识别交易记录存储达到上限监听失败"),
    FACE_ALI_UNINIT(2448, "阿里算法未初始化，请先初始化"),
    FACE_ALI_BLACKLIST(2449, "已欠费，请充值"),
    
    
    /** 补登相关  */
    RECHARGE_SUCCESS(2500, "补登成功"),
    RECHARGE_IS_NOT_SUPPORTED(2501, "此卡不支持补登"),
    RECHARGE_QUERY_ORDER(2502,"查询订单失败"),
    RECHARGE_THERE_IS_NO_ORDER(2503, "此卡不存在补登订单"),
    RECHARGE_LOAD_FAIL(2504,"圈存失败"),
    RECHARGE_CARD_LOST(2505, "闪卡"),
    RECHARGE_NETWORK_DISCONNECT(2506, "补登网络故障"),
    RECHARGE_APP_ERROR(2507,"补登应用选择失败"),
    RECHARGE_APDU_FAIL(2508, "请求APDU指令失败"),
    RECHARGE_SUCCESS_LAST_TIME(2509,"上次补登已经完成"),
    RECHARGE_STATUS_UNKNOWN(2510, "补登结果未知，请联系客服处理"),
    RECHARGE_UNSUPPORTED_TYPE(2511,"不支持的补登类型"),
    RECHARGE_MONEY_PROCESSING(2512, "正在补登"),
    RECHARGE_ANNUAL_PROCESSING(2513, "正在年审"),
    RECHARGE_SYSTEM_EXCEPTION(2514, "补登系统异常"),

    /** 卡码脸  */
    CARD_VALID_AND_QR_CODE_VALID(2601, "公交卡有效，乘车码有效，人脸验证通过"),
    CARD_VALID_AND_QR_CODE_INVALID(2602,"公交卡有效，乘车码失效，人脸验证通过"),
    CARD_NOT_ENOUGH_AND_QR_CODE_VALID(2603,"公交卡欠次，乘车码有效，人脸验证通过"),
    CARD_NOT_ENOUGH_AND_QR_CODE_INVALID(2604, "公交卡欠次，乘车码失效，人脸验证不通过"),
    CARD_INVALID_AND_QR_VALID(2605,"公交卡失效，乘车码有效，人脸验证通过"),
    CARD_INVALID_AND_QR_INVALID(2606, "公交卡失效 乘车码失效 人脸验证不通过"),
    CARD_AND_QR_STATUS_UNKNOWN(2607,"卡码脸状态未知"),

    /** 健康码相关  */
    HEALTH_COMM_FAIL(2701, "通讯失败"),
    HEALTH_ON_QUERY(2702, "正在查询"),
    HEALTH_QUERY_FAIL(2703,"查询失败"),
    HEALTH_RE_SWIPE(2704, "请重刷"),
    HEALTH_CARD_NOT_SUPPORT(2705, "不支持卡类型"),
    CARD_IS_NOT_REAL_NAME(2706,"卡片没有实名制"),
    CARD_IS_NOT_IN_WHITE_LIST(2707, "外部人员"),

    /** 超市消费相关  */
    CARD_NOT_SUPPORT(2801, "不支持的卡种"),
    CARD_QUERY_BALANCE_FAIL(2802,"查询余额失败"),
    CARD_QUERY_BALANCE_SUCCESS(2803, "查询余额成功"),
    CARD_TRADE_FAIL(2804, "交易失败"),
    KEYBOARD_SET_VALUE(2805,"设置按键值"),


    /** 功能相关 */
    /** 功能启用*/
    PAGE_CAN_NOT_CONSUME(3100, "此界面不支持消费"),
    PAGE_NOT_EXIST(3101, "页面不存在"),

    /** 身份证*/
    ID_CARD_OPEN_FAIL(3200, "打开身份证读卡器失败"),
    ID_CARD_CLOSE_FAIL(3201, "关闭身份证读卡器失败"),
    ID_CARD_OVER_TIME(3202, "身份证超时"),
    ID_CARD_READ_FAIL(3203, "读身份证失败"),
    ID_CARD_READ_FAIL_MULTIPLE(3204, "多次读身份证失败"),
    ID_CARD_OPERATE_FAIL(3205, "身份证操作失败"),
    ID_CARD_USB_OUT(3206, "USB被拔出"),
    ID_CARD_USB_FIND_FAIL(3207, "USB没有找到"),
    ID_CARD_UNSUPPORTED(3208,"不支持操作"),

    /** 商汤SDK错误码 */
    ST_SDK_INIT_SUCCESS(3400, "商汤SDK初始化完成"),
    ST_CONFIG_FILE_PARSE_FAILED(3401, "商汤配置文件解析失败"),
    ST_LICENSE_FILE_NOT_EXIST(3402, "证书文件不存在"),
    ST_MODEL_FILE_NOT_EXIST(3403, "模型文件不存在"),
    ST_ENCRYPT_CHIP_INIT_FAILED(3404, "加密芯片初始化失败"),
    ST_MODEL_FILE_LOAD_FAILED(3405, "模型文件加载失败");

    companion object{
        private val mMap = HashMap<Int, ErrorCodeEnum>()

        init {
            values().forEach { enum ->
                mMap[enum.code] = enum
            }
        }

        @Throws(IllegalArgumentException::class)
        fun getEnum(code : Int) : ErrorCodeEnum{
            return if(mMap.containsKey(code)){
                mMap[code] ?: throw IllegalArgumentException("错误码 $code 不存在")
            }else{
                throw IllegalArgumentException("错误码 $code 不存在")
            }
        }
    }
}