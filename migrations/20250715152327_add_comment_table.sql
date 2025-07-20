-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Comment] (
    [Id]         UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_Comment          PRIMARY KEY,
    [Content]    NVARCHAR(500)    NOT NULL,
    [CreatedTime]DATETIME,
    [UpdatedTime]DATETIME,
    [IsDeleted]  BIT              NOT NULL CONSTRAINT DF_Comment_IsDeleted DEFAULT 0,
    [Account]    UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Comment_Account   FOREIGN KEY REFERENCES [dbo].[Account]([Id]),
    [Section]    UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Comment_Section   FOREIGN KEY REFERENCES [dbo].[CommentSection]([Id])
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Comment];
-- +goose StatementEnd
