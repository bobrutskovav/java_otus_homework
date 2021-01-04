package ru.otus.multiprocess.backend.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.multiprocess.messagesystem.client.ResultDataType;

import java.util.List;

@Data
@AllArgsConstructor
public class AllUsersDto extends ResultDataType {
    private List<UserDto> allUsersDto;
}
