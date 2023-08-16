package com.goorm.profileboxapiadmin.service;

import com.goorm.profileboxcomm.dto.notice.NoticeDTO;
import com.goorm.profileboxcomm.dto.notice.request.CreateNoticeRequestDTO;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.entity.Notice;
import com.goorm.profileboxcomm.enumeration.FilmoType;
import com.goorm.profileboxcomm.exception.ApiException;
import com.goorm.profileboxcomm.exception.ExceptionEnum;
import com.goorm.profileboxcomm.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public List<Notice> getAllNoitce(){
        return noticeRepository.findAll();
    }

    @Transactional
    public Notice getSpecificNotice(Long noticeId){
        Notice notice = noticeRepository.findByNoticeId(noticeId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NOTICE_NOT_FOUND));
        return notice;
    }

    @Transactional
    public Notice registerNotice(CreateNoticeRequestDTO dto, Member member) throws ParseException {
        validate();
        Notice entity = CreateNoticeRequestDTO.toEntity(dto);
        entity.setMember(member);
        return noticeRepository.save(entity);
    }

    @Transactional
    public Notice updateNotice(NoticeDTO dto, Long noticeId){
        validate();
        final Notice origin = noticeRepository.findByNoticeId(noticeId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NOTICE_NOT_FOUND));

        if(dto.getTitle() != null) origin.setNoticeTitle(dto.getTitle());
        if(dto.getContent() != null) origin.setNoticeContent(dto.getContent());
        if(dto.getFilmo_type() != null) origin.setFilmoType(FilmoType.valueOf(dto.getFilmo_type()));
        if(dto.getFilmo_name() != null) origin.setFilmoName(dto.getFilmo_name());
        if(dto.getFilmo_role() != null) origin.setFilmoRole(dto.getFilmo_role());
        if(dto.getFilming_end_period() != null) origin.setFilmingEndPeriod(Date.valueOf(dto.getFilming_end_period()));
        if(dto.getFilming_start_period() != null) origin.setFilmingStartPeriod(Date.valueOf(dto.getFilming_start_period()));
        if(dto.getApply_deadline_dt() != null) origin.setApplyDeadlineDt(Date.valueOf(dto.getApply_deadline_dt()));

        return getSpecificNotice(noticeId);
    }

    @Transactional
    public void deleteNotice(Long noticeId){
        validate();
        try{
            noticeRepository.deleteByNoticeId(noticeId);
        }catch (Exception e){
            throw new RuntimeException("delete err: " + e.getMessage());
        }
    }

    private void validate(){
        // 구현해야함
    }
}
