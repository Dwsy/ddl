package link.dwsy.ddl.controller;

import link.dwsy.ddl.XO.RB.UserInfoRB;
import link.dwsy.ddl.XO.VO.UserInfoAndLevel;
import link.dwsy.ddl.annotation.AuthAnnotation;
import link.dwsy.ddl.core.CustomExceptions.CodeException;
import link.dwsy.ddl.core.constant.CustomerErrorCode;
import link.dwsy.ddl.core.domain.LoginUserInfo;
import link.dwsy.ddl.entity.User.User;
import link.dwsy.ddl.repository.User.UserFollowingRepository;
import link.dwsy.ddl.repository.User.UserRepository;
import link.dwsy.ddl.repository.User.UserTagRepository;
import link.dwsy.ddl.support.UserSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

@RestController
@RequestMapping("info")
public class UserInfoController {

    @Resource
    private UserRepository userRepository;
    @Resource
    private UserSupport userSupport;
    @Resource
    private UserFollowingRepository userFollowingRepository;



    @GetMapping
    @AuthAnnotation()
    public UserInfoAndLevel getUserInfo() {
        Long id = userSupport.getCurrentUser().getId();
        User user = userRepository.findUserByIdAndDeletedIsFalse(id);
        return new UserInfoAndLevel(user);
    }

    @GetMapping("{id}")
//    @AuthAnnotation()
    public User getUserById(@PathVariable Long id) {
        User user = userRepository.findUserByIdAndDeletedIsFalse(id);
        if (user != null) {
            LoginUserInfo currentUser = userSupport.getCurrentUser();
            //fixme
            if (currentUser != null) {
                Long currentUserId = currentUser.getId();
                boolean following = userFollowingRepository.existsByUserIdAndFollowingUserIdAndDeletedIsFalse(currentUserId, id);
                if (following) {
                    user.setFollowing(true);
                }
            }
            return user;
        } else {
            throw new CodeException(CustomerErrorCode.UserNotExist);
        }
    }

    @PutMapping
    public boolean updateUserInfo(@RequestBody UserInfoRB userInfo) {
        Long id = userSupport.getCurrentUser().getId();
        User user = userRepository.findUserByIdAndDeletedIsFalse(id);
        Optional.ofNullable(userInfo.getAvatar()).ifPresent(user.getUserInfo()::setAvatar);
        Optional.ofNullable(userInfo.getSign()).ifPresent(user.getUserInfo()::setSign);
        Optional.ofNullable(userInfo.getBirth()).ifPresent(user.getUserInfo()::setBirth);
        Optional.ofNullable(userInfo.getNickname()).ifPresent(user::setNickname);
        Optional.ofNullable(userInfo.getGender()).ifPresent(user.getUserInfo()::setGender);
        userRepository.save(user);
        return true;
    }


    @Resource
    private UserTagRepository userTagRepository;

//    @GetMapping
//    public String test() {
//        return userRepository.find(3L).getNickname();
//    }

    @GetMapping("exp")
    public int getUserExp() {
        Long id = userSupport.getCurrentUser().getId();
        return userRepository.getUserExpById(id);
    }
}
