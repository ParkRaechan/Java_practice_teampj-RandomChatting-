package RandomChatting.controller;

import RandomChatting.Service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/map")
public class MapController {
    @Autowired
    MapService mapService;

    @GetMapping("")
    public String map(){return "map/map";}

    @GetMapping("/getUsersLocation")
    @ResponseBody
    public void getUsersLocation(@RequestParam("X") String x, @RequestParam("Y") String y,
                                 @RequestParam("gender") String gender, @RequestParam("name") String name,
                                 HttpServletResponse response){
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(mapService.getUsersLocation(x, y, gender, name));
        }catch (Exception e){e.printStackTrace();}
    }
    @GetMapping("/getNearUser")
    @ResponseBody
    public void getNearUser(@RequestParam("X") String x, @RequestParam("Y") String y,
                            HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(mapService.nearUsers(x,y,1000).toString());
        }catch (Exception e){e.printStackTrace();}
    }
}
