package com.example.demo.rest;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;

@Path("/")
public class MemberResource {

	@Inject
	private MemberRepository repository;
	
	@Context 
	UriInfo uriInfo;
	
	@GET
	@Path("members/{id}")
	@Produces("application/vnd.qantas.find.member.v1+json")
	public Response findMemberv1(@PathParam(value="id") long memberId) {
		Optional<Member> member = repository.findMemberById(memberId);
		if(member.isPresent()) {
			return Response.ok()
						   .entity(member.get())
						   .build();
		}
		else {
			return Response
					.status(Status.NOT_FOUND)
					.build();
		}
	}
	
	@POST
	@Path("members")
	@Consumes("application/vnd.qantas.add.member.v1+json")
	public Response addMemberv1(Member member) {
		long memberId = repository.addMember(member);
		return Response	.created(uriInfo
						.getAbsolutePathBuilder()
						.path(String.valueOf(memberId))
						.build())
				.build();
	}
	
	@DELETE
	@Path("members/{id}")
	public Response removeMemberv1(@PathParam(value="id") long memberId) {
		if(repository.removeMember(memberId)) {
			return Response
					.ok()
					.build();
		}
		else {
			return Response
					.status(Status.NOT_FOUND)
					.build();
		}
	}
	
	@PUT
	@Path("members")
	@Consumes("application/vnd.qantas.update.member.v1+json")
	public Response updateMemberv1(@Context UriInfo uriInfo, Member member) {
		long memberId = repository.updateMember(member);
		return Response.created(uriInfo
						.getAbsolutePathBuilder()
						.path(String.valueOf(memberId))
						.build())
				.build();
	}
	
	
	@GET
	@Path("members")
	@Produces("application/vnd.qantas.findall.members.v1+json")
	public List<Member> findAllv1() {
		return repository.findAll();
	}
	
}
