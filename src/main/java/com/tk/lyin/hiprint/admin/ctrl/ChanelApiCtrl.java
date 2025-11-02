package com.tk.lyin.hiprint.admin.ctrl;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.tk.lyin.hiprint.admin.vo.R;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChanelApiCtrl extends BaseCtrl {
    //private final ChannelManager channelManager;

    private final SocketIOServer server;



    public R list() {
        return success();
    }

    @GetMapping("hello")
    public R get(String name, HttpServletRequest request) {
        return success(request.getSession().getId());
    }

    @GetMapping("/start")
    public R start() {
        server.start();
        return success();
    }

    @GetMapping("/shutdown")
    public R shutdown() {
        server.stop();
        return success();
    }

}
