package com.unfbx.chatgptsteamoutput.controller;

import cn.hutool.core.util.StrUtil;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgptsteamoutput.controller.request.ChatRequest;
import com.unfbx.chatgptsteamoutput.controller.response.ChatResponse;
import com.unfbx.chatgptsteamoutput.service.SseService;
import com.unfbx.chatgptsteamoutput.until.promptUntil.Prompt4SQL;
import com.unfbx.chatgptsteamoutput.until.promptUntil.SystemPrompt.Prompt4DataAsync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.unfbx.chatgptsteamoutput.until.openAIRequstUntil.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @date 2023-03-01
 */
@Controller
@Slf4j
public class ChatController {

    private final SseService sseService;

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());

    public ChatController(SseService sseService) {
        this.sseService = sseService;
    }

    /**
     * 创建sse连接
     *
     * @param headers
     * @return
     */
    @CrossOrigin
    @GetMapping("/createSse")
    public SseEmitter createConnect(@RequestHeader Map<String, String> headers) {
        String uid = getUid(headers);
        return sseService.createSse(uid);
    }

    /**
     * 聊天接口
     *
     * @param chatRequest
     * @param headers
     */
    @CrossOrigin
    @PostMapping("/chat")
    @ResponseBody
    public ChatResponse sseChat(@RequestBody ChatRequest chatRequest, @RequestHeader Map<String, String> headers, HttpServletResponse response) throws ExecutionException, InterruptedException {
        String uid = getUid(headers);
        String userQuestion = chatRequest.getMsg();
        //任务分为三步，1：生成SQL、数据查询 2：数据提取 3：文本回调
        CompletableFuture<String> genSQLData = CompletableFuture.supplyAsync(() -> {
            try {
                OpenAIRequest openAIRequest = new OpenAIRequest("SQL", userQuestion);
                System.out.println("进行了数据查询");
                return openAIRequest.getResult(); //当它是在SQL中查询的数据
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<String> asyncResult = genSQLData.thenApply(result -> Prompt4DataAsync.getPrompt() + result);
        System.out.println(asyncResult.get() + userQuestion);
        chatRequest.setMsg(asyncResult.get() + "问题为" + userQuestion);
        sseService.sseChat(uid, chatRequest);
        return new ChatResponse();
    }

    /**
     * 关闭连接
     *
     * @param headers
     */
    @CrossOrigin
    @GetMapping("/closeSse")
    public void closeConnect(@RequestHeader Map<String, String> headers) {
        String uid = getUid(headers);
        sseService.closeSse(uid);
    }

    @GetMapping("")
    public String index() {
        return "1.html";
    }

    @GetMapping("/websocket")
    public String websocket() {
        return "websocket.html";
    }

    /**
     * 获取uid
     *
     * @param headers
     * @return
     */
    private String getUid(Map<String, String> headers) {
        String uid = headers.get("uid");
        if (StrUtil.isBlank(uid)) {
            throw new BaseException(CommonError.SYS_ERROR);
        }
        return uid;
    }


}
