package life.majiang.community.service;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
/**
 * 当单一的dao层操作无法满足需要时，我们就需要在service中对原子性操作进行组合
 */
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {

        // 对超出边界的页码进行处理
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        // 查询总记录数(在此处打断点)
        Integer totalCount = questionMapper.count();

        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1){
            page = 1;
        }

        if (page > totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);

        // 查询的逻辑:size * (page - 1)
        Integer offset = size * (page - 1);

        List<Question> questions = questionMapper.list(offset,size);

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question :questions){
            User user = userMapper.findById(question.getCreator());// 获取当前关联的user对象
            QuestionDTO questionDTO = new QuestionDTO();

            // copyProperties()：
            //     Copy the property values of the given source bean into the target bean.
            BeanUtils.copyProperties(question,questionDTO);

            questionDTO.setUser(user);

            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    /**
     * 重载QuestionService的list()方法
     *
     * ProfileController调用了此方法
     */
    public PaginationDTO list(Integer userId, Integer page, Integer size) {

        // 对超出边界的页码进行处理
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        // 查询总记录数(在此处打断点)
        Integer totalCount = questionMapper.countByUserId(userId);

        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1){
            page = 1;
        }

        if (page > totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);

        // 查询的逻辑:size * (page - 1)
        Integer offset = size * (page - 1);

        // 修改此方法
        List<Question> questions = questionMapper.listByUserId(userId,offset,size);

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question :questions){
            User user = userMapper.findById(question.getCreator());// 获取当前关联的user对象
            QuestionDTO questionDTO = new QuestionDTO();

            // copyProperties()：
            //     Copy the property values of the given source bean into the target bean.
            BeanUtils.copyProperties(question,questionDTO);

            questionDTO.setUser(user);

            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);

        // 将查询结果传递给前端

        return paginationDTO;
    }
}