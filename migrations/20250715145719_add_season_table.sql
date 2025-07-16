-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Season] (
    [Id]           UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_Season          PRIMARY KEY,
    [Title]        NVARCHAR(100)    NOT NULL,
    [ReleaseDate]  DATE,
    [CreatedTime]  DATETIME,
    [UpdatedTime]  DATETIME,
    [Description]  NVARCHAR(1000),
    [IsDeleted]    BIT NOT NULL CONSTRAINT DF_Season_IsDeleted DEFAULT 0,
    [Show]         UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Season_Show FOREIGN KEY REFERENCES [dbo].[Show]([Id])
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Season];
-- +goose StatementEnd
