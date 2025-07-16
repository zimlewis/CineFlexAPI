-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[ReportComment] (
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    [Content] NVARCHAR(500) NOT NULL,
    [Status] INT,
    [CreatedTime] DATETIME,
    [UpdatedTime] DATETIME,
    [Account] UNIQUEIDENTIFIER FOREIGN KEY REFERENCES [dbo].[Account]([Id]),
    [Comment] UNIQUEIDENTIFIER FOREIGN KEY REFERENCES [dbo].[Comment]([Id])
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[ReportComment];
-- +goose StatementEnd
