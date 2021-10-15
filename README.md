# Goal:

1. Login 
2. List news
3. Edit profile
4. Note - (goole keep)


# Petunjuknya: 
Start !

1. project leader : buat project baru di git nya. (master / develop)
2. add collaborator tim nya
3. tim nya, clone project pakai url -> master / develop
4. branch nya di kerjakan masing masing
5. push, lalu buat MR / PR
6. si PL akan memeriksa koding, dan kemudian accept

# Aturan Push
	repository : Github / Gitlab

	branch : 
	master -> fix / release
	develop -> beta testing, project leader
		feat -> fitur baru
		fix -> fixing error / bug
		style -> UI / UX

	pertama kali clone : master, sebelum mengerjakan itu harus pindah branch

	task : ui register page

	buat branch baru : git checkout -b feat/ui-register-page

	git add -A
	git commit -m "feat [AF] ui register page"
	git push origin feat/ui-register-page

	membuat merge request / pull request -> menggabungkan koding ke develop. dari feat/ui-register-page menuju ke develop

	project leader nya : itu hanya akan click accept, sebelumnya di cek dulu
