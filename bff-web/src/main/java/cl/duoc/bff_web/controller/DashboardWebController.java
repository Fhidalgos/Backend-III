package cl.duoc.bff_web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.bff_web.dto.DashboardWebResponse;
import cl.duoc.bff_web.service.DashboardWebService;

@RestController
@RequestMapping("/api/web")
public class DashboardWebController {

    private final DashboardWebService dashboardWebService;

    public DashboardWebController(
            DashboardWebService dashboardWebService) {

        this.dashboardWebService = dashboardWebService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardWebResponse> obtenerDashboard() {

        return ResponseEntity.ok(
                dashboardWebService.obtenerDashboard()
        );
    }
}