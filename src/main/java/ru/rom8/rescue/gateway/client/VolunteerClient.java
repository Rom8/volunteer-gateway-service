package ru.rom8.rescue.gateway.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.rom8.rescue.gateway.api.VolunteerApi;
import ru.rom8.rescue.gateway.api.model.VolunteerDto;
import ru.rom8.rescue.gateway.api.model.VolunteerRegisterRequest;
import ru.rom8.rescue.gateway.api.model.VolunteerUpdateRequest;

@HttpExchange(accept = "application/json", contentType = "application/json")
public interface VolunteerClient {

    @GetExchange(VolunteerApi.PATH_GET_ME)
    VolunteerDto getMe(@RequestHeader("X-USER-ID") String userId);

    @PostExchange(VolunteerApi.PATH_REGISTER_ME)
    VolunteerDto registerMe(@RequestHeader("X-USER-ID") String userId, @RequestBody VolunteerRegisterRequest request);

    @PatchExchange(VolunteerApi.PATH_UPDATE_ME)
    VolunteerDto updateMe(
            @RequestHeader("X-USER-ID") String userId,
            @RequestBody VolunteerUpdateRequest request
    );
}
