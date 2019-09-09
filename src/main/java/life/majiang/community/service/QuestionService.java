package life.majiang.community.service;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.QuestionExample;
import life.majiang.community.model.User;
import org.apache.ibatis.session.RowBounds;
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

        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());

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

        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question :questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());// 获取当前关联的user对象
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

        QuestionExample questionExample = new QuestionExample();

        questionExample.createCriteria()
                .andCreatorEqualTo(userId);

        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());

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
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question :questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());// 获取当前关联的user对象
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

    /**
     * getById：根据id，去question表查询指定的记录
     */
    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);

        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);

        User user = userMapper.selectByPrimaryKey(question.getCreator());// 获取当前关联的user对象
        questionDTO.setUser(user);

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getCreator() == null){
            questionMapper.insert(question);
        }else {
            // 更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());

            questionMapper.updateByExampleSelective(updateQuestion,example);
        }
    }
}