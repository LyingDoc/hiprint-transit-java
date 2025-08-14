package com.tk.lyin.hiprint.admin;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tk.lyin.hiprint.admin.vo.R;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChanelApiController extends BaseController{
    //private final ChannelManager channelManager;
    private final SocketIOServer server;

    public R list() {
        return success();
    }

    @GetMapping("hello")
    public R get(String name) {
        return success(name);
    }

    @GetMapping("shutdown")
    public R shutdown() {
        server.stop();
        return success();
    }
}
