package com.service;

import java.util.List;

import com.entity.ContactMessage;
import com.entity.JobPost;
import com.entity.MediaAsset;
import com.entity.MediaType;
import com.entity.SiteSettings;
import com.entity.TeamMember;
import com.io.ContactMessageRequest;
import com.io.JobPostRequest;
import com.io.MediaAssetRequest;
import com.io.SiteSettingsRequest;
import com.io.TeamMemberRequest;

public interface WebsiteService {

	SiteSettings getSettings();

	SiteSettings updateSettings(SiteSettingsRequest request);

	List<MediaAsset> listPublicMedia(MediaType mediaType);

	List<MediaAsset> listAllMedia();

	MediaAsset createMedia(MediaAssetRequest request);

	MediaAsset updateMedia(Long id, MediaAssetRequest request);

	void deleteMedia(Long id);

	List<JobPost> listPublicJobs();

	List<JobPost> listAllJobs();

	JobPost createJob(JobPostRequest request);

	JobPost updateJob(Long id, JobPostRequest request);

	void deleteJob(Long id);

	List<TeamMember> listPublicTeam();

	List<TeamMember> listAllTeam();

	TeamMember createTeamMember(TeamMemberRequest request);

	TeamMember updateTeamMember(Long id, TeamMemberRequest request);

	void deleteTeamMember(Long id);

	ContactMessage submitContact(ContactMessageRequest request);

	List<ContactMessage> listContacts(String status);

	ContactMessage updateContactStatus(Long id, String status);

	void deleteContact(Long id);
}
