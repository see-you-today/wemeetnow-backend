package com.example.chat.repository;

import com.example.chat.domain.User;
import com.example.chat.domain.result.MultipleResult;
import com.example.chat.domain.result.SingleResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query("select u from User u join fetch u.authorities a where u.username = :username")
    Optional<User> findByUsernameWithAuthority(String username);

    @Service
    @Transactional(readOnly = true)
    class ResponseService {

        public <T> SingleResult<T> getSingleResult(T data) {
            SingleResult<T> result = new SingleResult<>();
            setSuccessResult(result);
            result.setData(data);

            return result;
        }

        public <T> MultipleResult<T> getMultipleResult(List<T> data) {
            MultipleResult<T> result = new MultipleResult<>();
            setSuccessResult(result);
            result.setData(data);

            return result;
        }

        public Result getSuccessResult() {
            Result result = new Result();
            setSuccessResult(result);

            return result;
        }

        public void setSuccessResult(Result result) {
            result.setSuccess(true);
            result.setCode(0);
            result.setMsg("성공");
        }

        public Result getFailureResult(int code, String msg) {
            Result result = new Result();
            result.setSuccess(false);
            result.setCode(code);
            result.setMsg(msg);

            return result;
        }
}
