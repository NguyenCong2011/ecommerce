package dev.cong.v.springcomereme.dao;


import dev.cong.v.springcomereme.request.CreateMomoRequest;
import dev.cong.v.springcomereme.response.CreateMomoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "momo", url = "${momo.end-point}")
public interface MomoApi {

    @PostMapping("/create")
    CreateMomoResponse createPayment(@RequestBody CreateMomoRequest request);
}
