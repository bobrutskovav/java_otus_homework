package ru.otus.ms.app.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.messagesystem.client.ResultDataType;

import java.util.List;

@Data
@AllArgsConstructor
public class AllUsersDto extends ResultDataType {
    private List<UserDto> allUsersDto;
}
