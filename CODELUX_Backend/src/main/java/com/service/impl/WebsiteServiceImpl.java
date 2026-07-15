package com.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.entity.ContactMessage;
import com.entity.ContactStatus;
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
import com.repository.ContactMessageRepository;
import com.repository.JobPostRepository;
import com.repository.MediaAssetRepository;
import com.repository.SiteSettingsRepository;
import com.repository.TeamMemberRepository;
import com.service.WebsiteService;

@Service
@Transactional
public class WebsiteServiceImpl implements WebsiteService {

	private static final long SETTINGS_ID = 1L;

	private final SiteSettingsRepository siteSettingsRepository;
	private final MediaAssetRepository mediaAssetRepository;
	private final JobPostRepository jobPostRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final ContactMessageRepository contactMessageRepository;

	public WebsiteServiceImpl(SiteSettingsRepository siteSettingsRepository, MediaAssetRepository mediaAssetRepository,
			JobPostRepository jobPostRepository, TeamMemberRepository teamMemberRepository,
			ContactMessageRepository contactMessageRepository) {
		this.siteSettingsRepository = siteSettingsRepository;
		this.mediaAssetRepository = mediaAssetRepository;
		this.jobPostRepository = jobPostRepository;
		this.teamMemberRepository = teamMemberRepository;
		this.contactMessageRepository = contactMessageRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public SiteSettings getSettings() {
		return siteSettingsRepository.findById(SETTINGS_ID).orElseGet(SiteSettings::new);
	}

	@Override
	public SiteSettings updateSettings(SiteSettingsRequest request) {
		SiteSettings settings = siteSettingsRepository.findById(SETTINGS_ID).orElseGet(SiteSettings::new);
		if (request.companyName() != null) {
			settings.setCompanyName(request.companyName());
		}
		if (request.tagline() != null) {
			settings.setTagline(request.tagline());
		}
		if (request.aboutTitle() != null) {
			settings.setAboutTitle(request.aboutTitle());
		}
		if (request.aboutDescription() != null) {
			settings.setAboutDescription(request.aboutDescription());
		}
		if (request.themePrimaryColor() != null) {
			settings.setThemePrimaryColor(request.themePrimaryColor());
		}
		if (request.themeSecondaryColor() != null) {
			settings.setThemeSecondaryColor(request.themeSecondaryColor());
		}
		if (request.logoUrl() != null) {
			settings.setLogoUrl(request.logoUrl());
		}
		if (request.faviconUrl() != null) {
			settings.setFaviconUrl(request.faviconUrl());
		}
		if (request.heroImageUrl() != null) {
			settings.setHeroImageUrl(request.heroImageUrl());
		}
		if (request.contactEmail() != null) {
			settings.setContactEmail(request.contactEmail());
		}
		if (request.contactPhone() != null) {
			settings.setContactPhone(request.contactPhone());
		}
		if (request.address() != null) {
			settings.setAddress(request.address());
		}
		if (request.facebookUrl() != null) {
			settings.setFacebookUrl(request.facebookUrl());
		}
		if (request.linkedinUrl() != null) {
			settings.setLinkedinUrl(request.linkedinUrl());
		}
		if (request.instagramUrl() != null) {
			settings.setInstagramUrl(request.instagramUrl());
		}
		return siteSettingsRepository.save(settings);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MediaAsset> listPublicMedia(MediaType mediaType) {
		if (mediaType == null) {
			return mediaAssetRepository.findByActiveTrueOrderBySortOrderAscCreatedAtDesc();
		}
		return mediaAssetRepository.findByMediaTypeAndActiveTrueOrderBySortOrderAscCreatedAtDesc(mediaType);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MediaAsset> listAllMedia() {
		return mediaAssetRepository.findAllByOrderBySortOrderAscCreatedAtDesc();
	}

	@Override
	public MediaAsset createMedia(MediaAssetRequest request) {
		MediaAsset media = new MediaAsset();
		applyMedia(media, request, true);
		return mediaAssetRepository.save(media);
	}

	@Override
	public MediaAsset updateMedia(Long id, MediaAssetRequest request) {
		MediaAsset media = mediaAssetRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Media asset not found"));
		applyMedia(media, request, false);
		return mediaAssetRepository.save(media);
	}

	@Override
	public void deleteMedia(Long id) {
		mediaAssetRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<JobPost> listPublicJobs() {
		return jobPostRepository.findByActiveTrueOrderByFeaturedDescCreatedAtDesc();
	}

	@Override
	@Transactional(readOnly = true)
	public List<JobPost> listAllJobs() {
		return jobPostRepository.findAllByOrderByCreatedAtDesc();
	}

	@Override
	public JobPost createJob(JobPostRequest request) {
		JobPost job = new JobPost();
		applyJob(job, request, true);
		return jobPostRepository.save(job);
	}

	@Override
	public JobPost updateJob(Long id, JobPostRequest request) {
		JobPost job = jobPostRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job post not found"));
		applyJob(job, request, false);
		return jobPostRepository.save(job);
	}

	@Override
	public void deleteJob(Long id) {
		jobPostRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeamMember> listPublicTeam() {
		return teamMemberRepository.findByActiveTrueOrderBySortOrderAscCreatedAtDesc();
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeamMember> listAllTeam() {
		return teamMemberRepository.findAllByOrderBySortOrderAscCreatedAtDesc();
	}

	@Override
	public TeamMember createTeamMember(TeamMemberRequest request) {
		TeamMember member = new TeamMember();
		applyTeamMember(member, request, true);
		return teamMemberRepository.save(member);
	}

	@Override
	public TeamMember updateTeamMember(Long id, TeamMemberRequest request) {
		TeamMember member = teamMemberRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team member not found"));
		applyTeamMember(member, request, false);
		return teamMemberRepository.save(member);
	}

	@Override
	public void deleteTeamMember(Long id) {
		teamMemberRepository.deleteById(id);
	}

	@Override
	public ContactMessage submitContact(ContactMessageRequest request) {
		requireText(request.fullName(), "Full name is required");
		requireText(request.email(), "Email is required");
		requireText(request.subject(), "Subject is required");
		requireText(request.message(), "Message is required");
		ContactMessage message = new ContactMessage();
		message.setFullName(request.fullName().trim());
		message.setEmail(request.email().trim());
		message.setPhone(request.phone());
		message.setSubject(request.subject().trim());
		message.setMessage(request.message().trim());
		message.setStatus(ContactStatus.NEW);
		return contactMessageRepository.save(message);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ContactMessage> listContacts(String status) {
		if (!StringUtils.hasText(status)) {
			return contactMessageRepository.findAllByOrderByCreatedAtDesc();
		}
		return contactMessageRepository.findByStatusOrderByCreatedAtDesc(parseStatus(status));
	}

	@Override
	public ContactMessage updateContactStatus(Long id, String status) {
		ContactMessage message = contactMessageRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact message not found"));
		message.setStatus(parseStatus(status));
		return contactMessageRepository.save(message);
	}

	@Override
	public void deleteContact(Long id) {
		contactMessageRepository.deleteById(id);
	}

	private void applyMedia(MediaAsset media, MediaAssetRequest request, boolean creating) {
		if (creating) {
			requireText(request.title(), "Media title is required");
			requireText(request.imageUrl(), "Image URL is required");
		}
		if (StringUtils.hasText(request.title())) {
			media.setTitle(request.title().trim());
		}
		if (request.caption() != null) {
			media.setCaption(request.caption());
		}
		if (StringUtils.hasText(request.mediaType())) {
			media.setMediaType(parseMediaType(request.mediaType()));
		}
		if (StringUtils.hasText(request.imageUrl())) {
			media.setImageUrl(request.imageUrl().trim());
		}
		if (request.linkUrl() != null) {
			media.setLinkUrl(request.linkUrl());
		}
		if (request.sortOrder() != null) {
			media.setSortOrder(request.sortOrder());
		}
		if (request.active() != null) {
			media.setActive(request.active());
		}
	}

	private void applyJob(JobPost job, JobPostRequest request, boolean creating) {
		if (creating) {
			requireText(request.title(), "Job title is required");
		}
		if (StringUtils.hasText(request.title())) {
			job.setTitle(request.title().trim());
		}
		if (request.location() != null) {
			job.setLocation(request.location());
		}
		if (request.employmentType() != null) {
			job.setEmploymentType(request.employmentType());
		}
		if (request.experience() != null) {
			job.setExperience(request.experience());
		}
		if (request.description() != null) {
			job.setDescription(request.description());
		}
		if (request.requirements() != null) {
			job.setRequirements(request.requirements());
		}
		if (request.applyEmail() != null) {
			job.setApplyEmail(request.applyEmail());
		}
		if (request.applyLink() != null) {
			job.setApplyLink(request.applyLink());
		}
		if (request.closingDate() != null) {
			job.setClosingDate(request.closingDate());
		}
		if (request.featured() != null) {
			job.setFeatured(request.featured());
		}
		if (request.active() != null) {
			job.setActive(request.active());
		}
	}

	private void applyTeamMember(TeamMember member, TeamMemberRequest request, boolean creating) {
		if (creating) {
			requireText(request.name(), "Team member name is required");
			requireText(request.designation(), "Designation is required");
		}
		if (StringUtils.hasText(request.name())) {
			member.setName(request.name().trim());
		}
		if (StringUtils.hasText(request.designation())) {
			member.setDesignation(request.designation().trim());
		}
		if (request.bio() != null) {
			member.setBio(request.bio());
		}
		if (request.imageUrl() != null) {
			member.setImageUrl(request.imageUrl());
		}
		if (request.linkedinUrl() != null) {
			member.setLinkedinUrl(request.linkedinUrl());
		}
		if (request.sortOrder() != null) {
			member.setSortOrder(request.sortOrder());
		}
		if (request.highlighted() != null) {
			member.setHighlighted(request.highlighted());
		}
		if (request.active() != null) {
			member.setActive(request.active());
		}
	}

	private MediaType parseMediaType(String value) {
		try {
			return MediaType.valueOf(value.trim().toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid media type");
		}
	}

	private ContactStatus parseStatus(String value) {
		try {
			return ContactStatus.valueOf(value.trim().toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid contact status");
		}
	}

	private void requireText(String value, String message) {
		if (!StringUtils.hasText(value)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
		}
	}
}
