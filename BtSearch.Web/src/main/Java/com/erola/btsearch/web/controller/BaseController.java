/**

                                                                                    +
                lbk                                                                @@
                                                                                  `@@@@@
                                                                       +@'  .:+@@@;@@@@@@
                                                                    @@@@@@@@@@+:      `#@@:
                                                               ,'@@@@@@@@@,`.`;``.; ```` @@@@
                                                           @#@@@@@@@@@@@``.`````;:@+,`````': ``
                                                             :@@@@@@@@ ``````` :,@@@ ;`` ````` ,
                                                                @@@@@@@ .````. .@@@..`. `````: `
                                                               ;````` `.`````.    '``````` ,,@.`
                                                              .`..`, `..`````````````````.@@@;`;
                        ,;:,..,:.::::::;::,`                   , .......``````````````.`:,@@,`:
                @`````````````````````````````````` ``     `.,;,,;..:,..```````,````,``'.::`'
                  :,;@ ````````````````````````````````````````````....`.`````````````````.+
                   `:;,`````````````````````````````````````````````....`,:`````````````` +
                  .`.```.....`':.````````````..................```````......`.,;;.``,;;`
                                                        + `...``````````........`
                                                   @@@+#;..``````````````````` ``,
                                      ` :.,:,.+. @@+`  '#.```````````````````  ``
                                    .,'''' ``..`@@@@@'+++'```````````````` ; ``
                      `,:,,::,,;,.'''#++'`......@@@@@@'++;',````````````;.````;
                `::::.......`''+++''''''++`.``...;@@@@@@+  `` `.``` ,:`..``` .
           `::,::,......````.+++''+''+++'''+#'##+``:@@@@@@@@@+;:;,.....`````
        `,:::,,.........``.```#''+++''++''+++''+++``` ,,:      ;`....```` '
    .,,,::,,:::,,::..,:...`..,;,````.+''++++'''+'''',`'      ```....````;
                `` ``.;:,.....`````````..,.,                   ``..``` `
                     ```.,:::,,,,,,,,,::`                       ;;.```'
                                                                  ,```



 */
package com.erola.btsearch.web.controller;

import com.erola.btsearch.web.model.AjaxResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller父类
 * Created by Erola on 2017/9/30.
 */
public abstract class BaseController {
    /**
     * 全局异常捕捉
     * @param request
     * @param response
     * @param exception
     * @return
     */
    @ExceptionHandler
    public String exception(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        //添加异常处理逻辑，如日志记录　　　







        boolean isAjaxRequest = false;
        if(request!=null){
            String xRequestedWith = request.getHeader("X-Requested-With");
            isAjaxRequest = xRequestedWith!=null && xRequestedWith.trim().equalsIgnoreCase("XMLHttpRequest");
        }
        if(isAjaxRequest){
            try {
                response.getWriter().write((new ObjectMapper()).writeValueAsString(AjaxResponseModel.getErrorResponse(exception.getMessage())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }else{
            //可以返回自定义的错误页面



            return "home/index";
        }
    }
}