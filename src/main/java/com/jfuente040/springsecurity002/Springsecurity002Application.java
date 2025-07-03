package com.jfuente040.springsecurity002;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jfuente040.springsecurity002.persistence.entity.PermissionEntity;
import com.jfuente040.springsecurity002.persistence.entity.PermissionEnum;
import com.jfuente040.springsecurity002.persistence.entity.RoleEntity;
import com.jfuente040.springsecurity002.persistence.entity.RoleEnum;
import com.jfuente040.springsecurity002.persistence.entity.UserEntity;
import com.jfuente040.springsecurity002.persistence.repository.PermissionRepository;
import com.jfuente040.springsecurity002.persistence.repository.RoleRepository;
import com.jfuente040.springsecurity002.persistence.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class Springsecurity002Application {

	public static void main(String[] args) {
		SpringApplication.run(Springsecurity002Application.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			/*
			 * =================================================
			 * INICIALIZAR PERMISOS
			 * =================================================
			 */
			Arrays.stream(PermissionEnum.values()).forEach(permissionEnum -> {
				permissionRepository.findByPermissionName(permissionEnum)
						.orElseGet(() -> {
							PermissionEntity permission = PermissionEntity.builder()
									.permissionName(permissionEnum)
									.description(permissionEnum.getDescription())
									.build();
							return permissionRepository.save(permission);
						});
			});

			/*
			 * =================================================
			 * INICIALIZAR ROLES
			 * =================================================
			 */
			// ROL ADMIN
			RoleEntity adminRole = roleRepository.findByRoleName(RoleEnum.ADMIN)
					.orElseGet(() -> {
						Set<PermissionEntity> allPermissions = Arrays.stream(PermissionEnum.values())
								.map(permissionEnum -> permissionRepository.findByPermissionName(permissionEnum).get())
								.collect(Collectors.toSet());

						RoleEntity role = RoleEntity.builder()
								.roleName(RoleEnum.ADMIN)
								.description(RoleEnum.ADMIN.getDescription())
								.permissions(allPermissions)
								.build();
						return roleRepository.save(role);
					});

			// ROL USER
			RoleEntity userRole = roleRepository.findByRoleName(RoleEnum.USER)
					.orElseGet(() -> {
						Set<PermissionEntity> userPermissions = new HashSet<>();
						userPermissions.add(permissionRepository.findByPermissionName(PermissionEnum.READ_CONTENT).get());
						userPermissions.add(permissionRepository.findByPermissionName(PermissionEnum.READ_USER).get());

						RoleEntity role = RoleEntity.builder()
								.roleName(RoleEnum.USER)
								.description(RoleEnum.USER.getDescription())
								.permissions(userPermissions)
								.build();
						return roleRepository.save(role);
					});

			// ROL MODERATOR
			RoleEntity moderatorRole = roleRepository.findByRoleName(RoleEnum.MODERATOR)
					.orElseGet(() -> {
						Set<PermissionEntity> moderatorPermissions = new HashSet<>();
						moderatorPermissions.add(permissionRepository.findByPermissionName(PermissionEnum.READ_CONTENT).get());
						moderatorPermissions.add(permissionRepository.findByPermissionName(PermissionEnum.UPDATE_CONTENT).get());
						moderatorPermissions.add(permissionRepository.findByPermissionName(PermissionEnum.READ_USER).get());
						moderatorPermissions.add(permissionRepository.findByPermissionName(PermissionEnum.UPDATE_USER).get());


						RoleEntity role = RoleEntity.builder()
								.roleName(RoleEnum.MODERATOR)
								.description(RoleEnum.MODERATOR.getDescription())
								.permissions(moderatorPermissions)
								.build();
						return roleRepository.save(role);
					});
			
			// ROL GUEST
			RoleEntity guestRole = roleRepository.findByRoleName(RoleEnum.GUEST)
					.orElseGet(() -> {
						Set<PermissionEntity> guestPermissions = new HashSet<>();
						guestPermissions.add(permissionRepository.findByPermissionName(PermissionEnum.READ_CONTENT).get());

						RoleEntity role = RoleEntity.builder()
								.roleName(RoleEnum.GUEST)
								.description(RoleEnum.GUEST.getDescription())
								.permissions(guestPermissions)
								.build();
						return roleRepository.save(role);
					});


			/*
			 * =================================================
			 * INICIALIZAR USUARIOS
			 * =================================================
			 */
			// USUARIO ADMIN
			userRepository.findByUsername("admin").orElseGet(() -> {
				UserEntity user = UserEntity.builder()
						.username("admin")
						.password(passwordEncoder.encode("admin123"))
						.email("admin@example.com")
						.firstName("Admin")
						.lastName("User")
						.roles(Set.of(adminRole))
						.build();
				return userRepository.save(user);
			});

			// USUARIO USER
			userRepository.findByUsername("user").orElseGet(() -> {
				UserEntity user = UserEntity.builder()
						.username("user")
						.password(passwordEncoder.encode("user123"))
						.email("user@example.com")
						.firstName("Regular")
						.lastName("User")
						.roles(Set.of(userRole))
						.build();
				return userRepository.save(user);
			});

			// USUARIO MODERATOR
			userRepository.findByUsername("moderator").orElseGet(() -> {
				UserEntity user = UserEntity.builder()
						.username("moderator")
						.password(passwordEncoder.encode("moderator123"))
						.email("moderator@example.com")
						.firstName("Moderator")
						.lastName("User")
						.roles(Set.of(moderatorRole))
						.build();
				return userRepository.save(user);
			});

			// USUARIO GUEST
			userRepository.findByUsername("guest").orElseGet(() -> {
				UserEntity user = UserEntity.builder()
						.username("guest")
						.password(passwordEncoder.encode("guest123"))
						.email("guest@example.com")
						.firstName("Guest")
						.lastName("User")
						.roles(Set.of(guestRole))
						.build();
				return userRepository.save(user);
			});
		};
	}
}
