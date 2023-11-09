package app.codingGround.api.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController{

    // 어드민 대시보드
    @GetMapping("/dashboard")
    public String AdminPage() {
        return "main/admin/dashboard";
    }

}
