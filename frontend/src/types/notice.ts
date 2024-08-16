export type Notification = {
	notificationId: number;
	studyId: number;
	studyName: string;
	createdAt: Date;
};

export type NotificationListResponse = {
	notifications: Notification[];
};

export type NotificationRequestDto = {
	notificationId: number;
	studyId: number;
	check: boolean;
};