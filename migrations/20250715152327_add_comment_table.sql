-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Comment] (
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    [Content] NVARCHAR(500) NOT NULL,
    [CreatedTime] DATETIME,
    [UpdatedTime] DATETIME,
    [IsDeleted] BIT NOT NULL DEFAULT 0,
    [Account] UNIQUEIDENTIFIER NOT NULL REFERENCES [dbo].[Account]([Id]),
    [Section] UNIQUEIDENTIFIER NOT NULL REFERENCES [dbo].[CommentSection]([Id])
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Comment];
-- +goose StatementEnd
