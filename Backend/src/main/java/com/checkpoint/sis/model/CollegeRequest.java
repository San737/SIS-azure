package com.checkpoint.sis.model;

import com.checkpoint.sis.enums.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "college_requests")
public class CollegeRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private Integer requestId;

	@Column(name = "college_name", nullable = false, length = 150)
	private String collegeName;

	@Column(name = "address", length = 255)
	private String address;

	@Column(name = "logo_url", length = 255)
	private String logoUrl;

	@ManyToOne(fetch = FetchType.LAZY) // Changed FetchType for consistency
	@JoinColumn(name = "requested_by", nullable = false)
	private User requestedBy;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false) // Removed redundant columnDefinition
	private Status status = Status.PENDING;

	@CreationTimestamp
	@Column(name = "request_date", updatable = false)
	private LocalDateTime requestDate;

	@ManyToOne(fetch = FetchType.LAZY) // Changed FetchType for consistency
	@JoinColumn(name = "approved_by")
	private User approvedBy;

	@Column(name = "approval_date")
	private LocalDateTime approvalDate;

	// --- NO 'College college' FIELD HERE ---

	// --- Getters and Setters ---

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public User getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(User requestedBy) {
		this.requestedBy = requestedBy;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public LocalDateTime getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(LocalDateTime approvalDate) {
		this.approvalDate = approvalDate;
	}
}

