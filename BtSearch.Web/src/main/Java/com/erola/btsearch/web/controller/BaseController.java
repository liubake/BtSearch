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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

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
    public ModelAndView exception(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        //添加异常处理逻辑，如日志记录　　　







        boolean isAjaxRequest = false;
        if(request!=null){
            String xRequestedWith = request.getHeader("X-Requested-With");
            isAjaxRequest = xRequestedWith!=null && xRequestedWith.trim().equalsIgnoreCase("XMLHttpRequest");
        }
        if(!isAjaxRequest){
            //可以返回自定义的错误页面



            ModelAndView modelAndView = new ModelAndView("home/index");
            //ModelAndView modelAndView = new ModelAndView("redirect:/home/index");
            return modelAndView;
        }else{
            AjaxResponseModel errorModel = AjaxResponseModel.getErrorResponse(exception.getMessage());
            ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
            Class errorModelClass = errorModel.getClass();
            Field[] fieldArray = errorModelClass.getDeclaredFields();
            if(fieldArray!=null && fieldArray.length>0){
                for (Field field : fieldArray) {
                    field.setAccessible(true);
                    try {
                        modelAndView.addObject(field.getName(), field.get(errorModel));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return modelAndView;
        }
    }
}