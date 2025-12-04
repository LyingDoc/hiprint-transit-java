package com.tk.lyin.hiprint.admin.ctrl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.tk.lyin.hiprint.dto.TemplateVo;
import com.tk.lyin.hiprint.utils.gson.GsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.tk.lyin.hiprint.admin.vo.R;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChanelApiCtrl extends BaseCtrl {
    //private final ChannelManager channelManager;

    private final SocketIOServer server;

    @GetMapping("list")
    public R list() {
        return success();
    }

    @PostMapping("/hello")
    public R get(HttpServletRequest request) {

        Collection<SocketIOClient> clients = server.getAllClients();
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
