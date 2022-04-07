package com.zhr.case8;

import java.util.List;

/**
 * @Des:
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case8
 * @Author: zhr
 * @Date: 2022/3/28 14:03
 * @Version: V1.0
 */
interface DataParser<T> {

    void parseData(String input, List<T> output, List<String> errors);
}