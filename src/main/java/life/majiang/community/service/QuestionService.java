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

        // 查询总记录数
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount,page,size);

        if (page < 1){
            page = 1;
        }

        if (page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }

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
}