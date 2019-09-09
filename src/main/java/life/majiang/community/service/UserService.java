package life.majiang.community.service;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 想要修改方法或变量名时，记住使用shift+f6
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {

        // 在用户点击登录后，根据传入的AccountId，去数据库进行匹配查找
        // command+alt+左箭头：回到上次编辑的地方
        User dbUser = userMapper.findByAccountId(user.getAccountId());

        // 判断我们从数据库中取出的user和当前想要登录的user，是否匹配
        if (dbUser == null){
            // 插入一条新记录
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            // 更新
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setToken(user.getToken());// 使用UUID
            dbUser.setName(user.getName());
            user.setAvatarUrl(user.getAvatarUrl());

            userMapper.update(dbUser);
        }
    }
}