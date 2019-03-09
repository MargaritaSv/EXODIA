package exodia.service;

import exodia.domain.models.service.UserServiceModel;

public interface UserService {
    boolean registerUser(UserServiceModel userServiceModel);

    UserServiceModel logUser(UserServiceModel userServiceModel);
}
