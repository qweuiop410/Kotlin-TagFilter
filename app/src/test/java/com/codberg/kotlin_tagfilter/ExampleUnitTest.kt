package com.codberg.kotlin_tagfilter

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val value1:String = "가 #가가 #각가가 ㅏㄱ ㅏ가각 가가가 가가 #가#나 #다 #@라 @#마 ##바 ####@#@#@#@#@@#@#사 \r #아 #자# # # #"
    // #, 출력 : 가가, 각가가, 가, 나, 다, 마, 바, 사, 아, 자
    // @, 출력 : 라
    val value2:String = "#가"
    // #, 출력 : 가
    // @, 출력 : null
    val value3:String = "\r\r\r\r\r      #   \r ###########       #@#@#@#@#@#@@#@   !@#@#@#"
    // #, 출력 : null
    // @, 출력 : null
    val value4:String = "#$#$ #@@#@#@ !#!##!# #%^#%#^ @#^&#^#&^#& #&*#&#*&# 8@#(#*)#(#)(# #_)#(_)#(_)#(#)_(#)#()#(#*#(#*"
    // #, 출력 : $, $, !, !, %^, %, ^, ^&, ^, &^, &, &*, &, *&, (, *), (, )(, _), (_), (_), (, )_(, ), (), (, *, (, *
    // @, 출력 : null
    val value5:String = "@가@나 @다 @@@@@@ 라#랄 !$!$!$!$!@#@#@\r#@##마 ##@#\r@###@#@#@## #@#@#@#\r@###@@@##바 @@#@#&*(&)(*&*()#@"
    // #, 출력 : 랄, 마, 바, #&*(&)(*&*()
    // @, 출력 : 가, 나, 다
    val value6:String = ""
    // #, 출력 : null
    // @, 출력 : null
    val value7:String = "@!#"
    // #, 출력 : null
    // @, 출력 : !
    val value8:String = "!#"
    // #, 출력 : null
    // @, 출력 : null


    @Test
    fun Testfun(){
        // 사용
        System.out.println("최종 출력 : " + StringSplit(value5,'#'))
    }

    fun StringSplit(value:String, splitTag:Char):ArrayList<String>?{
        // 중간 저장할 문자열
        var tempHashTags = ArrayList<String>()
        // 최종 저장할 문자열
        var hashTags = ArrayList<String>()

        // 분리할 문자를 파라미터 splitTag으로 지정
        val splitValue : Char = splitTag
        // 분리할 문자열이 #이면 @, @면 # (사람테그인지 해시테그인지 판별)
        val _splitValue :Char
        if(splitTag == '#') _splitValue = '@' else _splitValue = '#'

        // splitValue을 기준으로 분리후 배열에 저장
        var tempValueList  = value.split(splitValue)
        // 분리된 문자열중 첫번째는 첫번째 splitValue가 나오기 전이라 제거
        tempValueList = tempValueList.drop(1)

        // 분리된 문자열 중간 가공
        for (str in tempValueList)
        {
            // 나눠진 문자열이 null이 아니고 첫글자가 공백이 아니면 실행
            if(str.length > 0 && str[0] != ' ') {
                // 분리된 문자열의 앞뒤 공백 제거
                var strTrim = str.trim()

                // 분리된 문자열이 공백이 아니면
                if (strTrim.length > 0) {
                    // 문자열의 길이가 2 이상이면 공백이 있는지 검사
                    if (strTrim.length > 2) {
                        // 공백이 있으면 공백을 기준으로 분리
                        var str = strTrim.split(" ")
                        // 분리된 첫번째 단어만 저장
                        tempHashTags.add(str[0])
                    } else {
                        tempHashTags.add(strTrim)
                    }
                }
            }
        }

        for(str in tempHashTags)
        {
            // 첫글자가 _splitValue이면 한번더 루프
            if (str[0] == _splitValue)
                StringSplit(str, splitValue)
            else
            {
                // 걸러낸 테그에 반대되는 테그가 있으면 분리 (사람 테그일때 : 가# -> 가)
                var _tagFilter = str.split(_splitValue)
                hashTags.add(_tagFilter[0])
            }
        }

        // 반환할 테그가 있으면 반환, 없으면 null반환
        if(hashTags.size > 0)
            return  hashTags
        else
            return  null
    }
}